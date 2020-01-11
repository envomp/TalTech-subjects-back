package ee.taltech.agonaudid.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "subject_comment")
public class SubjectComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;

	private OffsetDateTime time;

	@Column(length = 2000)
	private String comment;

	protected SubjectComment() {
	}

	public SubjectComment(User user, Subject subject, OffsetDateTime time, String comment) {
		this.user = user;
		this.subject = subject;
		this.time = time;
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public Subject getSubject() {
		return subject;
	}
}
