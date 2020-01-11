package ee.taltech.agonaudid.pojo.api.reviews;

import ee.taltech.agonaudid.domain.SubjectRating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectReviewPostDto {

	private Long userId;
	private String review;
	private SubjectRating rating;
	private String token;

}
