package techcourse.fakebook.service.notification.assembler;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.service.notification.dto.NotificationResponse;
import techcourse.fakebook.service.user.UserService;

@Component
public class NotificationAssembler {
    private int maxMessageLength = 25;
    private final UserService userService;

    public NotificationAssembler(UserService userService) {
        this.userService = userService;
    }

    public NotificationResponse chat(long srcUserId, String content) {
        return produce(NotificationResponse.Type.CHAT, srcUserId, content);
    }

    public NotificationResponse friendRequest(long srcUserId) {
        return produce(NotificationResponse.Type.FRIEND_REQUEST, srcUserId, null);
    }

    public NotificationResponse comment(long srcUserId, Article destArticle) {
        return produce(NotificationResponse.Type.COMMENT, srcUserId, contentSummary(destArticle));
    }

    public NotificationResponse like(long srcUserId, Article destArticle) {
        return produce(NotificationResponse.Type.LIKE, srcUserId, contentSummary(destArticle));
    }

    private NotificationResponse produce(NotificationResponse.Type type, long srcUserId, String content) {
        return new NotificationResponse(type, this.userService.getUserOutline(srcUserId), content);
    }

    private int setMaxMessageLength(int length) {
        this.maxMessageLength = length;
        return length;
    }

    public int getMaxMessageLength() {
        return this.maxMessageLength;
    }

    private String contentSummary(String content) {
        return (content.length() > this.maxMessageLength)
                ? content.substring(0, this.maxMessageLength - 4) + " ..."
                : content;
    }

    private String contentSummary(Article article) {
        return contentSummary(article.getContent());
    }
}