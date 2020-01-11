package ee.taltech.agonaudid.pojo.api.reviews;

import ee.taltech.agonaudid.domain.SubjectRating;
import ee.taltech.agonaudid.domain.SubjectReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SubjectReviewDto {

	private Long id;
	private Long userId;
	private String username;
	private Long subjectId;
	private SubjectRating rating;
	private OffsetDateTime time;
	private String review;

	public SubjectReviewDto(SubjectReview review) {
		this.id = review.getId();
		this.userId = review.getUser().getId();
		this.username = review.getUser().getUsername();
		this.subjectId = review.getSubject().getId();
		this.rating = review.getRating();
		this.time = review.getTime();
		this.review = review.getReview();
	}

}
