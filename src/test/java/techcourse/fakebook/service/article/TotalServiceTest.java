package techcourse.fakebook.service.article;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import techcourse.fakebook.service.ServiceTestHelper;
import techcourse.fakebook.service.article.dto.TotalArticleResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TotalServiceTest extends ServiceTestHelper {
    @Autowired
    private TotalService totalService;

    @Test
    void 게시글들을_잘_불러오는지_확인한다() {
        List<TotalArticleResponse> articles = totalService.findArticlesByUser(1L);
        assertThat(articles.size()).isGreaterThanOrEqualTo(1);
    }
}