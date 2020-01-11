package ee.taltech.agonaudid.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String code;

	private String courseName;
	private String courseNameEng;
	private Integer eap;
	private String semesters;

	@OneToMany(mappedBy = "subject")
	private Set<SubjectReview> reviews = new HashSet<>();

	@OneToMany(mappedBy = "subject")
	private Set<SubjectComment> comments = new HashSet<>();

	@ManyToMany(mappedBy = "subjects")
	private Set<User> users = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(
			name = "teacher_subject",
			joinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"))
	private Set<Teacher> teachers = new HashSet<>();

	protected Subject() {
	}

	public Subject(String code, String courseName, String courseNameEng, Integer eap, String semesters) {
		this.code = code;
		this.courseName = courseName;
		this.courseNameEng = courseNameEng;
		this.eap = eap;
		this.semesters = semesters;
	}

	public Subject(String code, String courseName) {
		this(code, courseName, "bigus potatus", 3, "allofthem");
	}

	public void addUser(User user) {
		users.add(user);
	}

	public void addTeacher(Teacher user) {
		teachers.add(user);
	}
}
