package techcourse.fakebook.web.controller.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techcourse.fakebook.service.notification.NotificationChannel;
import techcourse.fakebook.service.notification.NotificationService;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.web.argumentresolver.SessionUser;

@RestController
@RequestMapping("/api/notification")
public class NotificationApiController {
    private final NotificationService notificationService;

    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<NotificationChannel> init(@SessionUser UserOutline user) {
        return ResponseEntity.ok().body(this.notificationService.issueNewChannelTo(user.getId()));
    }

    @GetMapping("/test")
    public ResponseEntity<NotificationChannel> test(@SessionUser UserOutline user) {
        final long destId = user.getId();
        return this.notificationService._getChannelOf(destId).map(channel -> {
            this.notificationService.notifyTo(
                    destId,
                    this.notificationService.writeChatMessageFrom(destId, "Hello from the other side :)")
            );
            return ResponseEntity.ok().body(channel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}