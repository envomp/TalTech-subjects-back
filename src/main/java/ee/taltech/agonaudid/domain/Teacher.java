package ee.taltech.agonaudid.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "teachers")
public class Teacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String name;

	@ManyToMany(mappedBy = "teachers")
	private Set<Subject> subjects = new HashSet<>();

	protected Teacher() {
	}

	public Teacher(String name) {
		this.name = name;
	}

	public void addSubject(Subject subject) {
		subjects.add(subject);
	}
}
