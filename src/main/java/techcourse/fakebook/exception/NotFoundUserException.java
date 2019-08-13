package techcourse.fakebook.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException() {
        super("존재하지 않는 사용자입니다.");
    }
}
