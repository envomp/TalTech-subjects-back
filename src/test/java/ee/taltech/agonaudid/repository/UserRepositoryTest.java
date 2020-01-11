package ee.taltech.agonaudid.repository;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.Teacher;
import ee.taltech.agonaudid.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository users;

	@Autowired
	private SubjectRepository subjects;

	@Autowired
	private TeacherRepository teachers;

	private User ago = new User("agooniaOn", "MinuPale");
	private Teacher teacherAgo = new Teacher("ago");
	private Subject agoSubject = new Subject("Jah", "aga ei");

	@Before
	public void fillSomeDataIntoOurDb() {
		// Add new Users to Database
		entityManager.persist(ago);
		entityManager.persist(teacherAgo);
		entityManager.persist(agoSubject);
	}

	@Test
	public void testFindByFirstName() throws Exception {
		// Search for specific User in Database according to username
		Optional<User> user = users.findByUsername("agooniaOn");
		assert user.isPresent();
		assert user.get().equals(ago);
	}

	@Test
	public void testUserAndSubject() throws Exception {
		ago.addSubject(agoSubject);
		agoSubject.addUser(ago);

		assertThat(ago.getSubjects(), contains(agoSubject));
		assertThat(subjects.findAll(), contains(agoSubject));
		assertThat(users.findByUsername(ago.getUsername()).get().getSubjects(), contains(agoSubject));
		assertThat(agoSubject.getUsers(), contains(ago));
	}

	@Test
	public void testTeacherAndSubject() throws Exception {
		teacherAgo.addSubject(agoSubject);
		agoSubject.addTeacher(teacherAgo);

		assertThat(teacherAgo.getSubjects(), contains(agoSubject));
		assertThat(subjects.findAll(), contains(agoSubject));

		assertThat(agoSubject.getTeachers(), contains(teacherAgo));
	}
}
