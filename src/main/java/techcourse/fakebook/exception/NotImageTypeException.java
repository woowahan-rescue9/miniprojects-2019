package techcourse.fakebook.exception;

public class NotImageTypeException extends RuntimeException{
    public NotImageTypeException() {
        super("이미지 파일 형식이 아닙니다.");
    }
}
