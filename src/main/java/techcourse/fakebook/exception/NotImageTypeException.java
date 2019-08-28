package techcourse.fakebook.exception;

public class NotImageTypeException extends RuntimeException{
    public NotImageTypeException() {
        super();
    }

    public NotImageTypeException(String message) {
        super(message);
    }
}
