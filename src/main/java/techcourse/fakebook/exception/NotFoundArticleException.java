package techcourse.fakebook.exception;

public class NotFoundArticleException extends RuntimeException {
    public NotFoundArticleException() {
        super();
    }

    public NotFoundArticleException(String message) {
        super(message);
    }
}
