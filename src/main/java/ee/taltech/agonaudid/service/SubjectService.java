package ee.taltech.agonaudid.service;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.exception.SubjectNotFoundException;
import ee.taltech.agonaudid.repository.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SubjectService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private final SubjectRepository subjectRepository;

	public SubjectService(SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}

	public List<Long> getAllSubjectCodes() {
		LOG.info("Reading all subject codes from database.");
		return subjectRepository.findAllOnlyIds();
	}

	public Subject getSubject(long id) {
		Optional<Subject> subject = subjectRepository.findById(id);
		if (subject.isPresent()) {
			LOG.info("Reading subject code " + subject.get().getCode() + " from database.");
			return subject.get();
		}
		LOG.error(String.format("Subject with id %s was not found.", id));
		throw new SubjectNotFoundException(String.format("No subject with code: %s was not found", id));
	}

	public List<Subject> getFilteredSubjects(
			String name,
			List<Long> ids,
			Integer page,
			Integer psize,
			String sort,
			Integer r
	) {
		LOG.info("Reading subjects by filters from database.");
		String nameFinal = name == null ? "" : name.toLowerCase();
		Stream<Subject> stream;
		if (ids != null) {
			stream = subjectRepository.findAllById(ids).stream();
		}
		else {
			stream = subjectRepository.findAll().stream();
		}
		if (!nameFinal.equals("")) {
			stream = stream.filter(x -> x.getCourseName().toLowerCase().contains(nameFinal) || x.getCourseNameEng().toLowerCase().contains(nameFinal) || x.getCode().toLowerCase().contains(nameFinal));
		}
		Comparator<Subject> comparator;
		sort = sort == null ? "" : sort;
		switch (sort) {
			case "name":
				comparator = Comparator.comparing(Subject::getCourseName);
				break;
			case "engname":
				comparator = Comparator.comparing(Subject::getCourseNameEng);
				break;
			case "eap":
				comparator = Comparator.comparing(Subject::getEap);
				break;
			default:
				comparator = Comparator.comparing(Subject::getCode);
				break;
		}
		comparator = r != null && r > 0 ? comparator.reversed() : comparator;
		page = page == null ? 0 : page;
		psize = psize == null ? 20 : psize;
		int skip = page * psize;
		return stream.sorted(comparator).skip(skip).limit(psize).collect(Collectors.toList());
	}
}
