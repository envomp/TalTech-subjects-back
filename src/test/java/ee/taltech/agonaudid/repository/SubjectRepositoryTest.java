package ee.taltech.agonaudid.repository;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.Teacher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubjectRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SubjectRepository subjects;

	@Autowired
	private TeacherRepository teachers;

	private Teacher teacherAgo = new Teacher("ago");
	private Subject agoSubject = new Subject("Jah", "aga ei");

	@Before
	public void fillSomeDataIntoOurDb() {
		// Add new Users to Database
		entityManager.persist(teacherAgo);
		entityManager.persist(agoSubject);
	}


	@Test
	public void testTeacherAndSubject() throws Exception {
		teacherAgo.addSubject(agoSubject);
		agoSubject.addTeacher(teacherAgo);

		assertThat(teacherAgo.getSubjects(), contains(agoSubject));
		assertThat(subjects.findAll(), contains(agoSubject));

		assertThat(agoSubject.getTeachers(), contains(teacherAgo));
		assert (subjects.findByCode("Jah").get().equals(agoSubject));
	}
}
