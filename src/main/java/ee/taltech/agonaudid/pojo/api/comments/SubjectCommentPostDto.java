package ee.taltech.agonaudid.pojo.api.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectCommentPostDto {

	private long userId;
	private String comment;
	private String token;

}
