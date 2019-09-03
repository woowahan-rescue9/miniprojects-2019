package techcourse.fakebook.service.chat.assembler;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.chat.Chat;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.chat.dto.ChatRequest;
import techcourse.fakebook.service.chat.dto.ChatResponse;

@Component
public class ChatAssembler {

    public ChatResponse toChatResponse(Chat chat) {
        return new ChatResponse(chat.getId(), chat.getFromUser().getId(), chat.getFromUser().getName(), chat.getContent(), chat.getReadable());
    }

    public Chat toEntity(ChatRequest chatRequest, User fromUser, User toUser) {
        return new Chat(chatRequest.getContent(), fromUser, toUser, false);
    }
}
