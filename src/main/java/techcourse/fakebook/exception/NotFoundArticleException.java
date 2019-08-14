package techcourse.fakebook.exception;

public class NotFoundArticleException extends RuntimeException {
    public NotFoundArticleException() {
        super("존재하지 않는 글입니다.");
    }
}
