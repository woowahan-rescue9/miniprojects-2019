package techcourse.fakebook.service.utils;

import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.UserDto;

public class ArticleAssembler {
    private ArticleAssembler() {}

    public static Article toEntity(ArticleRequest articleRequest, User user) {
        return new Article(articleRequest.getContent(), user);
    }

    public static ArticleResponse toResponse(Article article) {
        UserDto userDto = UserAssembler.toDto(article.getUser());
        return new ArticleResponse(article.getId(), article.getContent(), userDto);
    }
}
