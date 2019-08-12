package techcourse.fakebook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;

import static org.assertj.core.api.Assertions.assertThat;

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
}