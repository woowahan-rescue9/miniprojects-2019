package techcourse.fakebook.service.comment.dto;

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
