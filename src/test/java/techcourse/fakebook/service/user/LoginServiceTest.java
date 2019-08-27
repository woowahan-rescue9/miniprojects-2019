package techcourse.fakebook.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.exception.NotMatchPasswordException;
import techcourse.fakebook.service.ServiceTestHelper;
import techcourse.fakebook.service.article.AttachmentService;
import techcourse.fakebook.service.article.dto.AttachmentResponse;
import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.service.user.encryptor.Encryptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
class LoginServiceTest extends ServiceTestHelper {
    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private Encryptor encryptor;

    @Test
    void login_틀린_이메일() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("notExist@email.com", "Password1!");
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundUserException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void login_틀린_비밀번호() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("exist@email.com", "WrongPassword1!");
        User user = mock(User.class);
        given(userRepository.findByEmail(loginRequest.getEmail()))
                .willReturn(Optional.of(user));
        given(encryptor.matches(loginRequest.getPassword(), user.getEncryptedPassword()))
                .willReturn(false);

        // Act & Assert
        assertThrows(NotMatchPasswordException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void login_올바른_이메일_및_비밀번호() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("exist@email.com", "Password1!");
        User user = mock(User.class);
        given(userRepository.findByEmail(loginRequest.getEmail()))
                .willReturn(Optional.of(user));
        given(encryptor.matches(loginRequest.getPassword(), user.getEncryptedPassword()))
                .willReturn(true);
        UserOutline expectedUserOutline = new UserOutline(1l, "name", new AttachmentResponse("a","a"));
        given(user.getId()).willReturn(expectedUserOutline.getId());
        given(user.getName()).willReturn(expectedUserOutline.getName());
        given(attachmentService.getAttachmentResponse(user.getProfileImage())).willReturn(expectedUserOutline.getProfileImage());

        // Act
        UserOutline userOutline = loginService.login(loginRequest);

        // Assert
        assertThat(userOutline).isEqualTo(expectedUserOutline);
    }
}