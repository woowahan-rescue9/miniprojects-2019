package techcourse.fakebook.service;

import io.restassured.internal.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.service.attachment.dto.AttachmentResponse;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
public class ServiceTestHelper {
    protected UserOutline userOutline;
    protected MultipartFile image;
    protected UserProfileImage userProfileImage;
    private AttachmentResponse userDefaultImage;

    @BeforeEach
    void setUp() throws IOException {
        File file = new File("src/test/resources/static/images/user/profile/default.png");
        FileInputStream input = new FileInputStream(file);
        image = new MockMultipartFile("file", file.getName(), "image/gif", IOUtils.toByteArray(input));
        userProfileImage = new UserProfileImage(image.getName(), "src/test/resources/static/images/user/profile/default.png");
        userOutline = new UserOutline(1l, "name", userDefaultImage);
    }
}
