package techcourse.fakebook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.fakebook.exception.NotFoundArticleException;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;

    @Test
    void 글을_잘_작성하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("내용입니다.");
        ArticleResponse articleResponse = articleService.save(articleRequest);

        assertThat(articleRequest.getContent()).isEqualTo(articleResponse.getContent());
    }

    @Test
    void 글을_잘_삭제하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("내용입니다.");
        ArticleResponse articleResponse = articleService.save(articleRequest);
        Long deletedId = articleResponse.getId();

        articleService.deleteById(deletedId);

        assertThrows(NotFoundArticleException.class, () -> articleService.findById(deletedId));
    }

    @Test
    void 글을_잘_수정하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("내용입니다.");
        ArticleResponse articleResponse = articleService.save(articleRequest);
        ArticleRequest updatedRequest = new ArticleRequest("수정된 내용입니다.");

        ArticleResponse updatedArticle = articleService.update(articleResponse.getId(), updatedRequest);

        assertThat(updatedArticle.getContent()).isEqualTo(updatedRequest.getContent());
        assertThat(updatedArticle.getId()).isEqualTo(articleResponse.getId());
    }
}