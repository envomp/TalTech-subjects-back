package ee.taltech.agonaudid.service;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.SubjectComment;
import ee.taltech.agonaudid.domain.User;
import ee.taltech.agonaudid.exception.SubjectNotFoundException;
import ee.taltech.agonaudid.exception.UserNotFoundException;
import ee.taltech.agonaudid.pojo.api.comments.SubjectCommentPostDto;
import ee.taltech.agonaudid.repository.SubjectCommentRepository;
import ee.taltech.agonaudid.repository.SubjectRepository;
import ee.taltech.agonaudid.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectCommentService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final UserRepository userRepository;
	private final SubjectRepository subjectRepository;
	private final SubjectCommentRepository subjectCommentRepository;

	public SubjectCommentService(UserRepository userRepository, SubjectRepository subjectRepository, SubjectCommentRepository subjectCommentRepository) {
		this.userRepository = userRepository;
		this.subjectRepository = subjectRepository;
		this.subjectCommentRepository = subjectCommentRepository;
	}


	public long addComment(long subject_id, SubjectCommentPostDto commentPost) {

		long user_id = commentPost.getUserId();
		String comment = commentPost.getComment();

		Optional<User> user = userRepository.findById(user_id);
		Optional<Subject> subject = subjectRepository.findById(subject_id);

		if (user.isEmpty()) {
			LOG.error(String.format("User with id %d was not found.", user_id));
			throw new UserNotFoundException(String.format("User with id %d was not found.", user_id));
		}

		if (subject.isEmpty()) {
			LOG.error(String.format("User with id %d was not found.", subject_id));
			throw new SubjectNotFoundException(String.format("User with id %d was not found.", subject_id));
		}

		SubjectComment subjectComment = new SubjectComment(user.get(), subject.get(), OffsetDateTime.now(), comment);
		subjectCommentRepository.save(subjectComment);
		LOG.info(subjectComment.toString() + " successfully saved into DB");
		return subjectComment.getId();
	}

	public List<SubjectComment> getSubjectComments(Long id) {
		LOG.info("Reading all comments for subject " + id + " from database.");
		return subjectCommentRepository.findBySubjectId(id);
	}

	public boolean verifyCommentAndUserMatch(long userId, long commentId) {
		return subjectCommentRepository.getOne(commentId).getUser().getId() == userId;
	}

	public void deleteSubjectComment(Long id) {
		LOG.info("Deleting comment " + id);
		subjectCommentRepository.deleteById(id);
	}

}
