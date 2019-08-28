package techcourse.fakebook.service.notification;

import org.springframework.stereotype.Component;
import techcourse.fakebook.service.user.UserService;

@Component
public class NotificationMessageFactory {
    private final UserService userService;

    public NotificationMessageFactory(UserService userService) {
        this.userService = userService;
    }

    public NotificationMessage chat(long sourceId, String content) {
        return produce(NotificationMessage.Type.CHAT, sourceId, content);
    }

    public NotificationMessage friendRequest(long sourceId) {
        return produce(NotificationMessage.Type.FRIEND_REQUEST, sourceId);
    }

    public NotificationMessage comment(long sourceId) {
        return produce(NotificationMessage.Type.COMMENT, sourceId);
    }

    public NotificationMessage like(long sourceId) {
        return produce(NotificationMessage.Type.LIKE, sourceId);
    }

    private NotificationMessage produce(NotificationMessage.Type type, long sourceId, String content) {
        return new NotificationMessage(type, this.userService.getUserOutline(sourceId), content);
    }

    private NotificationMessage produce(NotificationMessage.Type type, long sourceId) {
        return produce(type, sourceId, null);
    }
}