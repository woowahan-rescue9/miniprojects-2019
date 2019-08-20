package techcourse.fakebook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.exception.NotFoundArticleException;
import techcourse.fakebook.service.dto.ArticleLikeResponse;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.utils.ArticleAssembler;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;

    private UserOutline userDto = new UserOutline(1L, "cony", "https");

    @Test
    void 게시글들을_잘_불러오는지_확인한다() {
        List<ArticleResponse> articles = articleService.findAll();
        assertThat(articles.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void 없는_글을_찾을_때_예외를_잘_던지는지_확인한다() {
        assertThrows(NotFoundArticleException.class, () -> articleService.findById(0L));
    }

    @Test
    void 글을_잘_작성하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("내용입니다.");
        ArticleResponse articleResponse = articleService.save(articleRequest, userDto);

        assertThat(articleRequest.getContent()).isEqualTo(articleResponse.getContent());
    }

    @Test
    void 글을_잘_삭제하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("내용입니다.");
        ArticleResponse articleResponse = articleService.save(articleRequest, userDto);
        Long deletedId = articleResponse.getId();

        articleService.deleteById(deletedId, userDto);

        assertThrows(NotFoundArticleException.class, () -> articleService.findById(deletedId));
    }

    @Test
    void 글을_잘_수정하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("내용입니다.");
        ArticleResponse articleResponse = articleService.save(articleRequest, userDto);
        ArticleRequest updatedRequest = new ArticleRequest("수정된 내용입니다.");

        ArticleResponse updatedArticle = articleService.update(articleResponse.getId(), updatedRequest, userDto);

        assertThat(updatedArticle.getContent()).isEqualTo(updatedRequest.getContent());
        assertThat(updatedArticle.getId()).isEqualTo(articleResponse.getId());
    }

    @Test
    void 좋아요가_잘_등록되는지_확인한다() {
        ArticleLikeResponse articleLikeResponse = articleService.like(1L, userDto);
        assertThat(articleLikeResponse.isLiked()).isTrue();
    }

    @Test
    void 좋아요가_잘_취소되는지_확인한다() {
        articleService.like(3L, userDto);
        ArticleLikeResponse articleLikeResponse = articleService.like(3L, userDto);
        assertThat(articleLikeResponse.isLiked()).isFalse();
    }

    @Test
    void 좋아요_여부를_확인한다() {
        ArticleLikeResponse articleLikeResponse = articleService.isLiked(4L, userDto);
        assertThat(articleLikeResponse.isLiked()).isFalse();
    }

    @Test
    void 게시글의_좋아요_개수를_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("내용입니다.");
        UserOutline userOutline = new UserOutline(1L, "name", "coverUrl");
        ArticleResponse articleResponse = articleService.save(articleRequest, userOutline);
        Long articleId = articleResponse.getId();

        articleService.like(articleId, userOutline);

        assertThat(articleService.getLikeCountOf(articleId)).isEqualTo(1);

        articleService.like(articleId, userOutline);

        assertThat(articleService.getLikeCountOf(articleId)).isEqualTo(0);
    }
}