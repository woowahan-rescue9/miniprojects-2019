package techcourse.fakebook.service.dto;

public class ArticleLikeResponse {
    private Long articleId;
    private boolean isLiked;

    public ArticleLikeResponse(Long articleId, boolean isLiked) {
        this.articleId = articleId;
        this.isLiked = isLiked;
    }

    public Long getArticleId() {
        return articleId;
    }

    public boolean isLiked() {
        return isLiked;
    }
}
