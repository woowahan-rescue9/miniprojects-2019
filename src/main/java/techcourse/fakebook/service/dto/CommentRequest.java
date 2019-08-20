package techcourse.fakebook.service.dto;

public class CommentRequest {
    private String content;

    private CommentRequest() {
    }

    public CommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
