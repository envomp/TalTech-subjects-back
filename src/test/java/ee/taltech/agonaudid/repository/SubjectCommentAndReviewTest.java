package ee.taltech.agonaudid.repository;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.SubjectComment;
import ee.taltech.agonaudid.domain.SubjectRating;
import ee.taltech.agonaudid.domain.SubjectReview;
import ee.taltech.agonaudid.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubjectCommentAndReviewTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubjectRepository subjects;

	@Autowired
	private SubjectReviewRepository subjectReviews;

	@Autowired
	private SubjectCommentRepository subjectComments;

	private User ago;
	private Subject agoSubject;
	private SubjectReview review;
	private SubjectComment comment;

	@Before
	public void fillSomeDataIntoOurDb() {
		ago = new User("Ago", "Naut");
		agoSubject = new Subject("wasd", "wasdi sissejuhatus");
		review =
				new SubjectReview(
						ago, agoSubject, SubjectRating.POSITIVE, OffsetDateTime.now(), "ist gut jaa");
		comment = new SubjectComment(ago, agoSubject, OffsetDateTime.now(), "This is some long text.");
	}

	@Test
	public void testFillReviewAndGetReview() throws Exception {
		entityManager.persist(ago);
		entityManager.persist(agoSubject);
		entityManager.persist(review);
		ago.addReview(review);

		assertThat(ago.getReviews(), contains(review));
		assertThat(subjectReviews.findAll(), contains(review));
	}

	@Test
	public void testFillCommentAndGetComment() throws Exception {
		entityManager.persist(ago);
		entityManager.persist(agoSubject);
		entityManager.persist(comment);
		ago.addComment(comment);

		assertThat(ago.getComments(), contains(comment));
		assertThat(subjectComments.findAll(), contains(comment));
	}

	@Test
	public void testFillSubjectAndGetSubject() throws Exception {
		entityManager.persist(ago);
		entityManager.persist(agoSubject);

		assertThat(subjects.findAll(), contains(agoSubject));
	}

	@Test
	public void testAddReviewAndSubjectExists() throws Exception {
		entityManager.persist(ago);
		entityManager.persist(agoSubject);
		entityManager.persist(review);
		entityManager.persist(comment);
		ago.addReview(review);
		ago.addComment(comment);

		assertThat(subjects.findAll(), contains(agoSubject));

		assertThat(ago.getReviews(), contains(review));
		assertThat(subjectReviews.findAll(), contains(review));

		assertThat(ago.getComments(), contains(comment));
		assertThat(subjectComments.findAll(), contains(comment));

	}
}
