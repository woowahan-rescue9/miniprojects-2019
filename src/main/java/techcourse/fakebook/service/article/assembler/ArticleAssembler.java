package techcourse.fakebook.service.article.assembler;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.article.dto.ArticleRequest;
import techcourse.fakebook.service.article.dto.ArticleResponse;
import techcourse.fakebook.service.article.dto.TotalArticleResponse;
import techcourse.fakebook.service.attachment.dto.AttachmentResponse;
import techcourse.fakebook.service.comment.dto.CommentResponse;
import techcourse.fakebook.service.user.assembler.UserAssembler;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.List;

@Component
public class ArticleAssembler {
    private final UserAssembler userAssembler;

    public ArticleAssembler(UserAssembler userAssembler) {
        this.userAssembler = userAssembler;
    }

    public Article toEntity(ArticleRequest articleRequest, User user) {
        return new Article(articleRequest.getContent(), user);
    }

    public ArticleResponse toResponse(Article article) {
        UserOutline userOutline = userAssembler.toUserOutline(article.getUser());
        return new ArticleResponse(article.getId(), article.getContent(), article.getCreatedDate(), userOutline);
    }

    public ArticleResponse toResponse(Article article, List<AttachmentResponse> attachments) {
        UserOutline userOutline = userAssembler.toUserOutline(article.getUser());
        return new ArticleResponse(article.getId(), article.getContent(), article.getCreatedDate(), userOutline, attachments);
    }

    public TotalArticleResponse toTotalArticleResponse(ArticleResponse articleResponse, Integer countOfComment,
                                                       Integer countOfLike, List<CommentResponse> comments) {
        return new TotalArticleResponse(articleResponse, countOfComment, countOfLike, comments);
    }
}
