package techcourse.fakebook.service.chat;

import org.junit.jupiter.api.BeforeEach;
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
import techcourse.fakebook.service.chat.dto.ChatResponse;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.service.user.dto.UserSignupRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void findAll() {
        // Arrange
        ChatResponse chatResponse = mock(ChatResponse.class);
        Chat chat = mock(Chat.class);
        given(chatRepository.findAll()).willReturn(Arrays.asList(chat));
        given(chatAssembler.toChatResponse(chat)).willReturn(chatResponse);

        // Act
//        chatService.findAll();

        // Assert
        verify(chatAssembler).toChatResponse(chat);
    }

    @Test
    void save() {
        // Arrange
        ChatRequest chatRequest = mock(ChatRequest.class);
        UserOutline userOutline = mock(UserOutline.class);
        User fromUser = mock(User.class);
        User toUser = mock(User.class);
        Chat chat = mock(Chat.class);
        given(userService.getUser(userOutline.getId())).willReturn(fromUser);
        given(userService.getUser(chatRequest.getUserId())).willReturn(toUser);
        given(chatAssembler.toEntity(chatRequest, fromUser, toUser)).willReturn(chat);

        // Act
        chatService.save(userOutline, chatRequest);

        // Assert
        verify(chatRepository).save(chat);
    }
}