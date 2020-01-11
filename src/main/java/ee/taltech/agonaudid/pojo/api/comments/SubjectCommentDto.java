package ee.taltech.agonaudid.pojo.api.comments;

import ee.taltech.agonaudid.domain.SubjectComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SubjectCommentDto {

	private Long id;
	private Long userId;
	private String username;
	private Long subjectId;
	private OffsetDateTime time;
	private String comment;

	public SubjectCommentDto(SubjectComment comment) {
		this.id = comment.getId();
		this.userId = comment.getUser().getId();
		this.username = comment.getUser().getUsername();
		this.subjectId = comment.getSubject().getId();
		this.time = comment.getTime();
		this.comment = comment.getComment();
	}

}
