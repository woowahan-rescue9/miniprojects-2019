package techcourse.fakebook.exception;

public class InvalidAuthorException extends RuntimeException {
    public InvalidAuthorException() {
        super();
    }

    public InvalidAuthorException(String message) {
        super(message);
    }
}
