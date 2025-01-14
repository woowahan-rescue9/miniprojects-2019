package techcourse.fakebook.service.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.service.ServiceTestHelper;
import techcourse.fakebook.service.attachment.AttachmentService;
import techcourse.fakebook.service.user.assembler.UserAssembler;
import techcourse.fakebook.service.user.dto.UserResponse;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.service.user.dto.UserUpdateRequest;
import techcourse.fakebook.service.user.encryptor.Encryptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest extends ServiceTestHelper {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAssembler userAssembler;

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private Encryptor encryptor;

    @Test
    void save_유저_저장() {
        // Arrange
        UserSignupRequest userSignupRequest = mock(UserSignupRequest.class);
        User user = mock(User.class);
        given(userAssembler.toEntity(userSignupRequest, attachmentService.getDefaultProfileImage())).willReturn(user);

        // Act
        userService.save(userSignupRequest);

        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void findById_존재하는_유저_조회() {
        // Arrange
        Long existUserId = 1L;
        User user = mock(User.class);
        given(userRepository.findById(existUserId)).willReturn(Optional.of(user));
        given(userAssembler.toResponse(user)).willReturn(mock(UserResponse.class));

        // Act
        userService.findById(existUserId);

        // Assert
        verify(userAssembler).toResponse(user);
    }

    @Test
    void findById_존재하지_않는_유저_조회() {
        // Arrange
        Long notExistUserId = 1L;
        given(userRepository.findById(notExistUserId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundUserException.class, () ->
                userService.findById(notExistUserId));
    }

    @Test
    void update_존재하는_유저_유저_수정() {
        // Arrange
        User user = mock(User.class);
        UserProfileImage profileImage = mock(UserProfileImage.class);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(image, "updatedIntroduction",
                "updatedName", "!234Qwer");
        Long existUserId = 1L;
        given(userRepository.findById(existUserId)).willReturn(Optional.of(user));
        given(attachmentService.saveProfileImage(userUpdateRequest.getProfileImage())).willReturn(profileImage);
        given(encryptor.encrypt(userUpdateRequest.getPassword())).willReturn("aaaaaaaa");
        // Act
        userService.update(existUserId, userUpdateRequest);

        // Assert
        verify(user).updateModifiableFields(userUpdateRequest.getName(), "aaaaaaaa",
                userUpdateRequest.getIntroduction(), profileImage);
    }

    @Test
    void 비밀번호를_입력하지_않고_유저_수정() {
        // Arrange
        User user = mock(User.class);
        UserProfileImage profileImage = mock(UserProfileImage.class);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(image, "updatedIntroduction",
                "updatedName", null);
        Long existUserId = 1L;
        given(userRepository.findById(existUserId)).willReturn(Optional.of(user));
        given(attachmentService.saveProfileImage(userUpdateRequest.getProfileImage())).willReturn(profileImage);
        given(user.getEncryptedPassword()).willReturn("aaa");
        // Act
        userService.update(existUserId, userUpdateRequest);

        // Assert
        verify(user).updateModifiableFields(userUpdateRequest.getName(), "aaa",
                userUpdateRequest.getIntroduction(), profileImage);
    }

    @Test
    void update_존재하지_않는_유저_유저_수정() {
        // Arrange
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(image, "updatedIntroduction",
                "updatedName", "updatedPassword");
        Long notExistUserId = 1L;
        given(userRepository.findById(notExistUserId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundUserException.class, () ->
                userService.update(notExistUserId, userUpdateRequest));
    }

    @Test
    void delete_존재하는_유저_유저_삭제() {
        // Arrange
        User user = mock(User.class);
        Long existUserId = 1L;
        given(userRepository.findById(existUserId)).willReturn(Optional.of(user));

        // Act
        userService.deleteById(existUserId);

        // Assert
        verify(userRepository).delete(user);
    }

    @Test
    void delete_존재하지_않는_유저_유저_삭제() {
        // Arrange
        Long notExistUserId = 1L;
        given(userRepository.findById(notExistUserId)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundUserException.class, () ->
                userService.deleteById(notExistUserId));
    }

    @Test
    void hasNotUserWithEmail_존재하지않는_이메일입력() {
        // Arrange
        String notExistsEmail = "new@test.com";
        given(userRepository.findByEmail(notExistsEmail)).willReturn(Optional.empty());

        // Act & Assert
        assertThat(userService.hasNotUserWithEmail(notExistsEmail)).isTrue();
    }

    @Test
    void hasNotUserWithEmail_존재했던_이메일입력() {
        // Arrange
        String existsEmail = "new@test.com";
        User existsUser = mock(User.class);
        given(userRepository.findByEmail(existsEmail)).willReturn(Optional.of(existsUser));

        // Act & Assert
        assertThat(userService.hasNotUserWithEmail(existsEmail)).isFalse();
    }

    @Test
    void findUserNamesByKeyword_키워드를_통한_유저_이름_조회() {
        //Arrange
        String keyword = "abc";
        User user = mock(User.class);
        List<User> usersConstainingKeyword = Arrays.asList(user);
        given(userRepository.findByNameContaining(keyword)).willReturn(usersConstainingKeyword);

        //Act
        userService.findUserNamesByKeyword(keyword);

        //Assert
        verify(userAssembler).toResponse(user);
    }
}