package ee.taltech.agonaudid.pojo.api.reviews;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectReviewDeleteDto {

	private long reviewId;
	private String token;

}
