package techcourse.fakebook.exception;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {
    }

    public NotFoundCommentException(String message) {
        super(message);
    }
}
