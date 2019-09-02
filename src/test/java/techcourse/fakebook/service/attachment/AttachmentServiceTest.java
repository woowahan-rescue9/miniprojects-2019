package techcourse.fakebook.service.attachment;

import io.restassured.internal.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleRepository;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.exception.NotFoundArticleException;
import techcourse.fakebook.exception.NotImageTypeException;
import techcourse.fakebook.service.attachment.dto.AttachmentResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AttachmentServiceTest {
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void 파일을_잘_저장하는지_확인한다() throws IOException {
        File file = new File("src/test/resources/static/images/logo/res9-logo.gif");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/gif", IOUtils.toByteArray(input));
        Article article = articleRepository.findById(1L).orElseThrow(NotFoundArticleException::new);

        AttachmentResponse attachmentResponse = attachmentService.saveAttachment(multipartFile, article);

        assertThat(attachmentResponse.getName()).isEqualTo("res9-logo.gif");
        assertThat(attachmentResponse.getPath()).contains(".gif");
    }

    @Test
    void 유저_프로필을_잘_저장하는지_확인한다() throws IOException {
        File file = new File("src/test/resources/static/images/user/profile/default.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/png", IOUtils.toByteArray(input));
        Article article = articleRepository.findById(1L).orElseThrow(NotFoundArticleException::new);

        UserProfileImage profileImage = attachmentService.saveProfileImage(multipartFile);

        assertThat(profileImage.getPath()).contains(".png");
    }

    @Test
    void 이미지_형식이_아닐_때_에러를_발생하는지_확인한다() throws IOException {
        File file = new File("src/test/resources/static/images/text.txt");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
        Article article = articleRepository.findById(1L).orElseThrow(NotFoundArticleException::new);
        assertThrows(NotImageTypeException.class, () -> attachmentService.saveAttachment(multipartFile, article));
    }

    @Test
    void 유저_프로필_저장_시_이미지_형식이_아닐_때_에러를_발생하는지_확인한다2() throws IOException {
        File file = new File("src/test/resources/static/images/text.txt");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
        Article article = articleRepository.findById(1L).orElseThrow(NotFoundArticleException::new);
        assertThrows(NotImageTypeException.class, () -> attachmentService.saveProfileImage(multipartFile));
    }
}
