package techcourse.fakebook.web.controller.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.service.article.dto.ArticleRequest;
import techcourse.fakebook.service.article.dto.ArticleResponse;
import techcourse.fakebook.service.chat.ChatService;
import techcourse.fakebook.service.chat.dto.ChatRequest;
import techcourse.fakebook.service.chat.dto.ChatResponse;
import techcourse.fakebook.service.notification.NotificationChannel;
import techcourse.fakebook.service.notification.NotificationService;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.web.argumentresolver.SessionUser;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> create(@RequestBody ChatRequest chatRequest, @SessionUser UserOutline userOutline) {
        ChatResponse chatResponse = chatService.save(userOutline, chatRequest);
        return ResponseEntity.created(URI.create("/api/chats/" + chatResponse.getId())).body(chatResponse);
    }

    @GetMapping("/{toUserId}")
    public ResponseEntity<List<ChatResponse>> findMatchedChat(@PathVariable Long toUserId, @SessionUser UserOutline userOutline) {
        List<ChatResponse> chatResponses = chatService.findByFromUserAndToUser(userOutline, toUserId);
        return ResponseEntity.ok().body(chatResponses);
    }
}
