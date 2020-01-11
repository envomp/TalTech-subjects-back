package ee.taltech.agonaudid.domain;

import ee.taltech.agonaudid.hash.SHA512;
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
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String username;

	@Column(name = "password_hash")
	private String passwordHash;

	private String salt;
	private Role role;

	@OneToMany(mappedBy = "user")
	private Set<SubjectComment> comments = new HashSet<>();

	@OneToMany(mappedBy = "user")
	private Set<SubjectReview> reviews = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(
			name = "user_subject",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
	private Set<Subject> subjects = new HashSet<>();

	protected User() {
	}

	public User(String username, String password) {
		SHA512 sha512 = new SHA512();
		String salt = sha512.generateHash();
		String passwordHash = sha512.get_SHA_512_SecurePassword(password, salt);
		this.username = username;
		this.passwordHash = passwordHash;
		this.salt = salt;
		this.role = Role.USER;
	}

	public void addReview(SubjectReview subjectReview) {
		reviews.add(subjectReview);
	}

	public void addComment(SubjectComment subjectComment) {
		comments.add(subjectComment);
	}

	public void addSubject(Subject subscription) {
		subjects.add(subscription);
	}

	public void removeSubject(Subject subject) {
		subjects.remove(subject);
	}
}
