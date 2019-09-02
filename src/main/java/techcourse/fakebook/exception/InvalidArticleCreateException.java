package techcourse.fakebook.exception;

public class InvalidArticleCreateException extends RuntimeException {
    public InvalidArticleCreateException() {
        super("글이나 이미지를 작성해 주세요.");
    }
}
