package ee.taltech.agonaudid.pojo.api.users;

import ee.taltech.agonaudid.domain.Role;
import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.User;
import ee.taltech.agonaudid.pojo.api.comments.SubjectCommentDto;
import ee.taltech.agonaudid.pojo.api.reviews.SubjectReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

	private Long id;
	private String username;
	private Role role;
	private List<SubjectCommentDto> comments;
	private List<SubjectReviewDto> reviews;
	private List<Long> subjectIds;

	public UserDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.role = user.getRole();
		this.comments = user.getComments().stream().map(SubjectCommentDto::new).collect(Collectors.toList());
		this.reviews = user.getReviews().stream().map(SubjectReviewDto::new).collect(Collectors.toList());
		this.subjectIds = user.getSubjects().stream().map(Subject::getId).collect(Collectors.toList());
	}

}
