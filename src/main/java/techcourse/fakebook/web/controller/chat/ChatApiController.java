package techcourse.fakebook.web.controller.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.service.chat.ChatService;
import techcourse.fakebook.service.chat.dto.ChatRequest;
import techcourse.fakebook.service.chat.dto.ChatResponse;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.web.argumentresolver.SessionUser;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatApiController {
    private static final Logger log = LoggerFactory.getLogger(ChatApiController.class);

    private final ChatService chatService;

    public ChatApiController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> create(@RequestBody ChatRequest chatRequest, @SessionUser UserOutline userOutline) {
        log.debug("begin");
        ChatResponse chatResponse = chatService.save(userOutline, chatRequest);
        return ResponseEntity.created(URI.create("/api/chats/" + chatResponse.getId())).body(chatResponse);
    }

    @GetMapping("/{toUserId}")
    public ResponseEntity<List<ChatResponse>> findMatchedChat(@RequestParam(value = "first") Boolean first, @PathVariable Long toUserId, @SessionUser UserOutline userOutline) {
        log.debug("begin");
        log.debug("first : " + first);

        List<ChatResponse> chatResponses = chatService.findByFromUserAndToUser(first, userOutline, toUserId);
        return ResponseEntity.ok().body(chatResponses);
    }
}