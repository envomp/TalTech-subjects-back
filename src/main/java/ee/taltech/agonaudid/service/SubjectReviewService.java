package ee.taltech.agonaudid.service;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.SubjectRating;
import ee.taltech.agonaudid.domain.SubjectReview;
import ee.taltech.agonaudid.domain.User;
import ee.taltech.agonaudid.exception.SubjectNotFoundException;
import ee.taltech.agonaudid.exception.UserNotFoundException;
import ee.taltech.agonaudid.pojo.api.reviews.SubjectReviewPostDto;
import ee.taltech.agonaudid.repository.SubjectRepository;
import ee.taltech.agonaudid.repository.SubjectReviewRepository;
import ee.taltech.agonaudid.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectReviewService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private final UserRepository userRepository;
	private final SubjectRepository subjectRepository;
	private final SubjectReviewRepository subjectReviewRepository;

	public SubjectReviewService(SubjectReviewRepository subjectReviewRepository, SubjectRepository subjectRepository, UserRepository userRepository) {
		this.subjectReviewRepository = subjectReviewRepository;
		this.subjectRepository = subjectRepository;
		this.userRepository = userRepository;
	}

	public long addReview(long subject_id, SubjectReviewPostDto reviewPost) {
		long user_id = reviewPost.getUserId();
		SubjectRating rating = reviewPost.getRating();
		String review = reviewPost.getReview();

		Optional<User> user = userRepository.findById(user_id);
		Optional<Subject> subject = subjectRepository.findById(subject_id);

		if (user.isEmpty()) {
			LOG.error(String.format("User with id %d was not found.", user_id));
			throw new UserNotFoundException(String.format("User with id %d was not found.", user_id));
		}

		if (subject.isEmpty()) {
			LOG.error(String.format("Subject with id %d was not found.", subject_id));
			throw new SubjectNotFoundException(String.format("Subject with id %d was not found.", subject_id));
		}

		SubjectReview subjectReview = new SubjectReview(user.get(), subject.get(), rating, OffsetDateTime.now(), review);
		subjectReviewRepository.save(subjectReview);
		LOG.info(subjectReview.toString() + " successfully saved into DB");
		return subjectReview.getId();
	}

	public List<SubjectReview> getSubjectReviews(long id) {
		LOG.info("Reading reviews for subject " + id + " from database.");
		return subjectReviewRepository.findBySubjectId(id);
	}

	public boolean verifyReviewAndUserMatch(long userId, long reviewId) {
		return subjectReviewRepository.getOne(reviewId).getUser().getId() == userId;
	}

	public void deleteSubjectReview(Long id) {
		LOG.info("Deleting review " + id);
		subjectReviewRepository.deleteById(id);
	}

}
