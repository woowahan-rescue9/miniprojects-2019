package techcourse.fakebook.exception;

public class InvalidSignupException extends RuntimeException {
    public InvalidSignupException(String message) {
        super(message);
    }
}
