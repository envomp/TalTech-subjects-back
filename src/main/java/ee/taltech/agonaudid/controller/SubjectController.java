package ee.taltech.agonaudid.controller;

import ee.taltech.agonaudid.pojo.api.comments.SubjectCommentDeleteDto;
import ee.taltech.agonaudid.pojo.api.comments.SubjectCommentDto;
import ee.taltech.agonaudid.pojo.api.comments.SubjectCommentPostDto;
import ee.taltech.agonaudid.pojo.api.reviews.SubjectReviewDeleteDto;
import ee.taltech.agonaudid.pojo.api.reviews.SubjectReviewDto;
import ee.taltech.agonaudid.pojo.api.reviews.SubjectReviewPostDto;
import ee.taltech.agonaudid.pojo.api.subjects.SubjectDto;
import ee.taltech.agonaudid.service.SubjectCommentService;
import ee.taltech.agonaudid.service.SubjectReviewService;
import ee.taltech.agonaudid.service.SubjectService;
import ee.taltech.agonaudid.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping({"/api/subjects"})
public class SubjectController {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final SubjectCommentService subjectCommentService;
	private final SubjectReviewService subjectReviewService;
	private final SubjectService subjectService;
	private final TokenService tokenService;

	public SubjectController(SubjectReviewService subjectReviewService, SubjectService subjectService, SubjectCommentService subjectCommentService, TokenService tokenService) {
		this.subjectReviewService = subjectReviewService;
		this.subjectService = subjectService;
		this.subjectCommentService = subjectCommentService;
		this.tokenService = tokenService;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "")
	public Object getSubjects(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) List<Long> ids,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer psize,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) Integer r
	) {
		if (q == null) {
			LOG.info("Getting all subject ids.");
			return subjectService.getAllSubjectCodes();
		}
		LOG.info("Getting filtered subjects");
		return subjectService.getFilteredSubjects(name, ids, page, psize, sort, r).stream().map(SubjectDto::new).collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/{id}")
	public SubjectDto getSubjectByCode(@PathVariable("id") long id) {
		LOG.info("Getting subject {}.", id);
		return new SubjectDto(subjectService.getSubject(id));
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/{id}/comments")
	public List<SubjectCommentDto> getSubjectComments(@PathVariable("id") long id) {
		LOG.info("Getting all comments for subject {}.", id);
		return subjectCommentService.getSubjectComments(id).stream().map(SubjectCommentDto::new).collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/{id}/reviews")
	public List<SubjectReviewDto> getSubjectReviews(@PathVariable("id") long id) {
		LOG.info("Getting all reviews for subject {}.", id);
		return subjectReviewService.getSubjectReviews(id).stream().map(SubjectReviewDto::new).collect(Collectors.toList());
	}

	//token required
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path = "/{id}/comments")
	public long addNewComment(@PathVariable("id") long id, @RequestBody SubjectCommentPostDto comment) throws AuthenticationException {
		if (tokenService.verifyTokenIsCertainId(comment.getToken(), comment.getUserId())) {
			LOG.info("Posting a comment by user {} to subject {}.", comment.getUserId(), id);
			return subjectCommentService.addComment(id, comment);
		} else {
			throw new AuthenticationException("Token and user ID do not match");
		}
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path = "/{id}/reviews")
	public long addNewReview(@PathVariable("id") long id, @RequestBody SubjectReviewPostDto review) throws AuthenticationException {
		if (tokenService.verifyTokenIsCertainId(review.getToken(), review.getUserId())) {
			LOG.info("Posting a review by user {} to subject {}.", review.getUserId(), id);
			return subjectReviewService.addReview(id, review);
		} else {
			throw new AuthenticationException("Token and user ID do not match");
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}/comments")
	public void deleteSubjectComment(@PathVariable("id") long userId, @RequestBody SubjectCommentDeleteDto del) throws AuthenticationException {
		if (tokenService.verifyTokenIsAdmin(del.getToken()) || tokenService.verifyTokenIsCertainId(del.getToken(), userId) && subjectCommentService.verifyCommentAndUserMatch(userId, del.getCommentId())) {
			subjectCommentService.deleteSubjectComment(del.getCommentId());
		} else {
			throw new AuthenticationException("You lack permissions to delete comments from that user.");
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}/reviews")
	public void deleteSubjectReview(@PathVariable("id") long userId, @RequestBody SubjectReviewDeleteDto del) throws AuthenticationException {
		if (tokenService.verifyTokenIsAdmin(del.getToken()) || tokenService.verifyTokenIsCertainId(del.getToken(), userId) && subjectReviewService.verifyReviewAndUserMatch(userId, del.getReviewId())) {
			subjectReviewService.deleteSubjectReview(del.getReviewId());
		} else {
			throw new AuthenticationException("You lack permissions to delete reviews from that user.");
		}
	}
}
