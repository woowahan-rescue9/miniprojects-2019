package techcourse.fakebook.service.notification;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.service.user.UserService;

@Component
public class NotificationMessageFactory {
    private final UserService userService;

    public NotificationMessageFactory(UserService userService) {
        this.userService = userService;
    }

    public NotificationMessage chat(long srcUserId, String content) {
        return produce(NotificationMessage.Type.CHAT, srcUserId, content);
    }

    public NotificationMessage friendRequest(long srcUserId) {
        return produce(NotificationMessage.Type.FRIEND_REQUEST, srcUserId, null);
    }

    public NotificationMessage comment(long srcUserId, Article destArticle) {
        return produce(NotificationMessage.Type.COMMENT, srcUserId, contentSummary(destArticle));
    }

    public NotificationMessage like(long srcUserId, Article destArticle) {
        return produce(NotificationMessage.Type.LIKE, srcUserId, contentSummary(destArticle));
    }

    private NotificationMessage produce(NotificationMessage.Type type, long srcUserId, String content) {
        return new NotificationMessage(type, this.userService.getUserOutline(srcUserId), content);
    }

    private String contentSummary(String content) {
        return (content.length() > 25) ? content.substring(0, 21) + " ..." : content;
    }

    private String contentSummary(Article article) {
        return contentSummary(article.getContent());
    }
}