package ee.taltech.agonaudid.service;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.Teacher;
import ee.taltech.agonaudid.repository.SubjectRepository;
import ee.taltech.agonaudid.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubjectPullService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final SubjectRepository subjectRepository;
	private final TeacherRepository teacherRepository;

	public SubjectPullService(SubjectRepository subjectRepository, TeacherRepository teacherRepository) {
		this.subjectRepository = subjectRepository;
		this.teacherRepository = teacherRepository;
	}

	private InputStreamReader establishInput() {
		String CSV_URL = "https://ois.ttu.ee/pls/portal/ois2.otsing_exp?p_param_block=AINEREGISTER.WEB2&p_stbl=data_aineregister.a_qtbl&p_data_fn=data_aineregister.slct_qtbl(data_aineregister.a_qtbl%2C%3A1%2C%3A2%2C1)&p_title_text=&p_close=0&p_format=CSV&p_trans_id=";
		URL url;
		URLConnection connection;
		int maxTries = 3;
		while (true) {
			try {
				url = new URL(CSV_URL);
				connection = url.openConnection();
				return new InputStreamReader(connection.getInputStream(), "ISO-8859-4");
			}
			catch (IOException e) {
				if (--maxTries == 0) {
					LOG.error("Error accessing subjects from OIS!", e);
					return null;
				}
			}
		}
	}

	@Transactional
	public void pullSubjects() {
		InputStreamReader input = establishInput();
		if (input == null) {
			return;
		}
		BufferedReader buffer = null;
		String line;
		String csvSplitBy = ";";
		String nameRegex = "\\s*\\([a-zA-Z0-9\\s,\\.]*\\)\\s*";

		try {
			buffer = new BufferedReader(input);
			buffer.readLine(); // skip header lines
			buffer.readLine();
			Map<String, Subject> existingSubjects = subjectRepository.findAll().stream().collect(Collectors.toMap(Subject::getCode, s -> s));
			Map<String, Teacher> existingTeachers = teacherRepository.findAll().stream().collect(Collectors.toMap(Teacher::getName, t -> t));
			ArrayList<Subject> changedSubjects = new ArrayList<>();
			ArrayList<Teacher> newTeachers = new ArrayList<>();
			while ((line = buffer.readLine()) != null) {
				String[] cells = line.split(csvSplitBy);
				for (int i = 0; i < cells.length; i++) {
					cells[i] = cells[i].replaceAll("\"", "");
				}
				cells[3] = cells[3].replace(",00", "");

				// code / name / engName / eap / semester / names
				Subject newSubject;
				boolean changed = false;
				if (existingSubjects.containsKey(cells[0])) {
					newSubject = existingSubjects.get(cells[0]);
					if (!newSubject.getCourseName().equals(cells[1])) {
						changed = true;
						newSubject.setCourseName(cells[1]);
					}
					if (!newSubject.getCourseNameEng().equals(cells[2])) {
						changed = true;
						newSubject.setCourseNameEng(cells[2]);
					}
					if (!newSubject.getEap().equals(Integer.valueOf(cells[3]))) {
						changed = true;
						newSubject.setEap(Integer.valueOf(cells[3]));
					}
					if (!newSubject.getSemesters().equals(cells[4])) {
						changed = true;
						newSubject.setSemesters(cells[4]);
					}
				}
				else {
					newSubject = new Subject(cells[0], cells[1], cells[2], Integer.valueOf(cells[3]), cells[4]);
					changed = true;
				}
				String[] names = cells[5].split(nameRegex);
				Set<Teacher> subjectTeachers = new HashSet<>();
				for (String name : names) {
					if (name.equals("")) {
						continue;
					}
					Teacher newTeacher;
					if (!existingTeachers.containsKey(name)) {
						newTeacher = new Teacher(name);
						newTeachers.add(newTeacher);
						existingTeachers.put(name, newTeacher);
					}
					else {
						newTeacher = existingTeachers.get(name);
					}
					subjectTeachers.add(newTeacher);
				}
				if (!subjectTeachers.equals(newSubject.getTeachers())) {
					changed = true;
					newSubject.setTeachers(subjectTeachers);
				}
				if (changed) {
					changedSubjects.add(newSubject);
				}
			}
			teacherRepository.saveAll(newTeachers);
			subjectRepository.saveAll(changedSubjects);
		}
		catch (IOException e) {
			LOG.error("Error reading with buffered reader!", e);
		}
		finally {
			if (buffer != null) {
				try {
					buffer.close();
				}
				catch (IOException e) {
					LOG.error("Error closing buffered reader!", e);
				}
			}
		}
	}
}
