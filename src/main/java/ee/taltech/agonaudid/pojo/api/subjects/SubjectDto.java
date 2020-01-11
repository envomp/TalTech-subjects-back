package ee.taltech.agonaudid.pojo.api.subjects;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.pojo.api.reviews.SubjectReviewDto;
import ee.taltech.agonaudid.pojo.api.comments.SubjectCommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SubjectDto {

	private Long id;
	private String code;
	private String courseName;
	private String courseNameEng;
	private Integer eap;
	private String semesters;
	private List<SubjectReviewDto> reviews;
	private List<SubjectCommentDto> comments;
	private List<TeacherDto> teachers;

	public SubjectDto(Subject subject) {
		this.id = subject.getId();
		this.code = subject.getCode();
		this.courseName = subject.getCourseName();
		this.courseNameEng = subject.getCourseNameEng();
		this.eap = subject.getEap();
		this.semesters = subject.getSemesters();
		this.reviews = subject.getReviews().stream().map(SubjectReviewDto::new).collect(Collectors.toList());
		this.comments = subject.getComments().stream().map(SubjectCommentDto::new).collect(Collectors.toList());
		this.teachers = subject.getTeachers().stream().map(TeacherDto::new).collect(Collectors.toList());
	}

}
