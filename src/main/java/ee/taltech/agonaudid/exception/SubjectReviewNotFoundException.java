package ee.taltech.agonaudid.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubjectReviewNotFoundException extends RuntimeException {
	public SubjectReviewNotFoundException() {
		super();
	}

	public SubjectReviewNotFoundException(String message) {
		super(message);
	}

	public SubjectReviewNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
