package techcourse.fakebook.service;

import io.restassured.internal.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleRepository;
import techcourse.fakebook.exception.NotFoundArticleException;
import techcourse.fakebook.service.dto.AttachmentResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AttachmentServiceTest {
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void 파일을_잘_저장하는지_테스트_한다() throws IOException {
        File file = new File("src/test/resources/static/images/logo/res9-logo.gif");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/gif", IOUtils.toByteArray(input));
        Article article = articleRepository.findById(1L).orElseThrow(NotFoundArticleException::new);

        AttachmentResponse attachmentResponse = attachmentService.saveAttachment(multipartFile, article);

        assertThat(attachmentResponse.getName()).isEqualTo("res9-logo.gif");
        assertThat(attachmentResponse.getPath()).contains(".gif");
    }
}
