package techcourse.fakebook.exception;

public class InvalidUserPasswordException extends RuntimeException {
    public InvalidUserPasswordException() {
        super("비밀번호가 조건에 만족하지 않습니다.");
    }
}
