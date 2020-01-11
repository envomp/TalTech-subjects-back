package ee.taltech.agonaudid.pojo.api.subjects;

import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.Teacher;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TeacherDto {

	private Long id;
	private String name;
	private List<Long> subjectIds;

	public TeacherDto(Teacher teacher) {
		this.id = teacher.getId();
		this.name = teacher.getName();
		this.subjectIds = teacher.getSubjects().stream().map(Subject::getId).collect(Collectors.toList());
	}

}
