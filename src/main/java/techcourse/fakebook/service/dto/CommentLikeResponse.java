package techcourse.fakebook.service.dto;

public class CommentLikeResponse {
    private Long commentId;
    private boolean isLiked;

    public CommentLikeResponse(Long commentId, boolean isLiked) {
        this.commentId = commentId;
        this.isLiked = isLiked;
    }

    public Long getCommentId() {
        return commentId;
    }

    public boolean isLiked() {
        return isLiked;
    }
}
