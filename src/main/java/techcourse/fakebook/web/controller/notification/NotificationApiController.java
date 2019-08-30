package techcourse.fakebook.web.controller.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import techcourse.fakebook.service.notification.NotificationChannel;
import techcourse.fakebook.service.notification.dto.NotificationResponse;
import techcourse.fakebook.service.notification.NotificationService;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.web.argumentresolver.SessionUser;

@RestController
public class NotificationApiController {
    private final NotificationService notificationService;

    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/api/notification")
    public ResponseEntity<NotificationChannel> init(@SessionUser UserOutline user) {
        return ResponseEntity.ok().body(this.notificationService.issueNewChannelTo(user.getId()));
    }
}