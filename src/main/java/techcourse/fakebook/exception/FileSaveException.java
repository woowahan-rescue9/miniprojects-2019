package techcourse.fakebook.exception;

public class FileSaveException extends RuntimeException {
    public FileSaveException() {
        super("파일을 저장하는 과정에서 오류가 발생하였습니다.");
    }
}
