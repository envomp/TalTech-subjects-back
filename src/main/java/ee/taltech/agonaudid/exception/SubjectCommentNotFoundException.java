package ee.taltech.agonaudid.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubjectCommentNotFoundException extends RuntimeException {
	public SubjectCommentNotFoundException() {
		super();
	}

	public SubjectCommentNotFoundException(String message) {
		super(message);
	}

	public SubjectCommentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
