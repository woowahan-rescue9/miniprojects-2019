package techcourse.fakebook.service.article.dto;

import techcourse.fakebook.service.comment.dto.CommentResponse;

import java.util.List;

public class TotalArticleResponse {
    private ArticleResponse articleResponse;
    private Integer countOfComment;
    private Integer countOfLike;
    private List<CommentResponse> comments;

    public TotalArticleResponse(ArticleResponse articleResponse, Integer countOfComment, Integer countOfLike, List<CommentResponse> comments) {
        this.articleResponse = articleResponse;
        this.countOfComment = countOfComment;
        this.countOfLike = countOfLike;
        this.comments = comments;
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

    public List<CommentResponse> getComments() {
        return comments;
    }
}
