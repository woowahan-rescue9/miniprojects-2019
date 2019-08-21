package techcourse.fakebook.service.utils;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleMultipart;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.UserOutline;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ArticleAssembler {
    private ArticleAssembler() {
    }

    public Article toEntity(ArticleRequest articleRequest, User user) {
        return new Article(articleRequest.getContent(), user);
    }

    public ArticleResponse toResponse(Article article) {
        UserOutline userOutline = UserAssembler.toUserOutline(article.getUser());
        return new ArticleResponse(article.getId(), article.getContent(), getRecentDate(article), userOutline);
    }

    public ArticleResponse toResponse(Article article, List<ArticleMultipart> resources) {
        UserOutline userOutline = UserAssembler.toUserOutline(article.getUser());
        return new ArticleResponse(article.getId(), article.getContent(), getRecentDate(article), userOutline, resources);
    }

    private LocalDateTime getRecentDate(Article article) {
        Optional<LocalDateTime> recentDate = Optional.ofNullable(article.getModifiedDate());
        return recentDate.orElse(article.getCreatedDate());
    }
}
