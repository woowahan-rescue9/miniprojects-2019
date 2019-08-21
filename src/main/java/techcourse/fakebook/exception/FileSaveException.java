package techcourse.fakebook.exception;

public class FileSaveException extends RuntimeException {
    public FileSaveException() {
        super();
    }

    public FileSaveException(String message) {
        super(message);
    }
}
