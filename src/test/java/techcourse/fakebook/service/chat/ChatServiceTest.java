package techcourse.fakebook.service.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.fakebook.domain.chat.Chat;
import techcourse.fakebook.domain.chat.ChatRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.chat.assembler.ChatAssembler;
import techcourse.fakebook.service.chat.dto.ChatRequest;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class ChatServiceTest {
    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatAssembler chatAssembler;

    @Mock
    private UserService userService;

    @Test
    void 채팅_저장() {
        // Arrange
        Long fromUserId = 0L;
        UserOutline userOutline = mock(UserOutline.class);
        given(userOutline.getId()).willReturn(fromUserId);

        Long toUserId = 1L;
        ChatRequest chatRequest = mock(ChatRequest.class);
        given(chatRequest.getUserId()).willReturn(toUserId);

        User fromUser = mock(User.class);
        given(userService.getUser(fromUserId)).willReturn(fromUser);

        User toUser = mock(User.class);
        given(userService.getUser(toUserId)).willReturn(toUser);


        Chat chat = mock(Chat.class);
        given(chatAssembler.toEntity(chatRequest, fromUser, toUser)).willReturn(chat);


        // Act
        chatService.save(userOutline, chatRequest);

        // Assert
        verify(chatRepository).save(chat);
    }

    @Test
    void 해당_채팅방_채팅_조회() {
        // Arrange
        Long fromUserId = 0L;
        UserOutline userOutline = mock(UserOutline.class);
        given(userOutline.getId()).willReturn(fromUserId);

        Long toUserId = 1L;

        Chat chat = mock(Chat.class);
        given(chatRepository.findByFromUserAndToUserOrToUserAndFromUser(userOutline.getId(), toUserId)).willReturn(Arrays.asList(chat));

        // Act
        chatService.findByFromUserAndToUser(false, userOutline, toUserId);

        // Assert
        verify(chatAssembler).toChatResponse(chat);
    }
}