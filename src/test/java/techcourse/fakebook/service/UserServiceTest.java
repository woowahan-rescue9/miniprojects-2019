package techcourse.fakebook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.fakebook.domain.User;
import techcourse.fakebook.domain.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.service.dto.UserRequest;
import techcourse.fakebook.service.dto.UserResponse;
import techcourse.fakebook.utils.UserAssembler;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAssembler userAssembler;

    @Test
    void save_유저_저장() {
        // Arrange
        UserRequest userRequest = mock(UserRequest.class);
        User user = mock(User.class);
        given(userAssembler.toEntity(userRequest)).willReturn(user);

        // Act
        userService.save(userRequest);

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
}