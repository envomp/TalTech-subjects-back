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
import javax.persistence.UniqueConstraint;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(
		name = "subject_review",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "subject_id"})}
)
public class SubjectReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne()
	@JoinColumn(name = "subject_id")
	private Subject subject;

	private SubjectRating rating;

	private OffsetDateTime time;

	@Column(length = 2000)
	private String review;

	protected SubjectReview() {
	}

	public SubjectReview(
			User user, Subject subject, SubjectRating rating, OffsetDateTime time, String review) {
		this.user = user;
		this.subject = subject;
		this.rating = rating;
		this.time = time;
		this.review = review;
	}

	public User getUser() {
		return user;
	}

	public Subject getSubject() {
		return subject;
	}
}
