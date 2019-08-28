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

    public NotificationMessage comment(long srcUserId, Article srcArticle) {
        return produce(NotificationMessage.Type.COMMENT, srcUserId, articleSummary(srcArticle));
    }

    public NotificationMessage like(long srcUserId, Article srcArticle) {
        return produce(NotificationMessage.Type.LIKE, srcUserId, articleSummary(srcArticle));
    }

    private NotificationMessage produce(NotificationMessage.Type type, long srcUserId, String content) {
        return new NotificationMessage(type, this.userService.getUserOutline(srcUserId), content);
    }

    private String articleSummary(Article article) {
        final String content = article.getContent();
        return (content.length() > 10) ? content.substring(0, 6) + " ..." : content;
    }
}