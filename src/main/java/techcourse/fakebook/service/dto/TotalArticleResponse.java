package techcourse.fakebook.service.dto;

public class TotalArticleResponse {
    private ArticleResponse articleResponse;
    private Integer countOfComment;
    private Integer countOfLike;

    public TotalArticleResponse(ArticleResponse articleResponse, Integer countOfComment, Integer countOfLike) {
        this.articleResponse = articleResponse;
        this.countOfComment = countOfComment;
        this.countOfLike = countOfLike;
    }

    public ArticleResponse getArticleResponse() {
        return articleResponse;
    }

    public Integer getCountOfComment() {
        return countOfComment;
    }

    public Integer getCountOfLike() {
        return countOfLike;
    }
}
