package techcourse.fakebook.utils;

import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;

public class ArticleAssembler {
    private ArticleAssembler() {
    }

    public static Article toEntity(ArticleRequest articleRequest) {
        return new Article(articleRequest.getContent());
    }

    public static ArticleResponse toResponse(Article article) {
        return new ArticleResponse(article.getId(), article.getContent());
    }
}
