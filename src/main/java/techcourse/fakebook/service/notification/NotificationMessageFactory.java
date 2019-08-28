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

    public NotificationMessage chat(long sourceUserId, String content) {
        return produce(NotificationMessage.Type.CHAT, sourceUserId, content);
    }

    public NotificationMessage friendRequest(long sourceUserId) {
        return produce(NotificationMessage.Type.FRIEND_REQUEST, sourceUserId, null);
    }

    public NotificationMessage comment(long sourceUserId, Article sourceArticle) {
        return produce(NotificationMessage.Type.COMMENT, sourceUserId, articleSummary(sourceArticle));
    }

    public NotificationMessage like(long sourceUserId, Article sourceArticle) {
        return produce(NotificationMessage.Type.LIKE, sourceUserId, articleSummary(sourceArticle));
    }

    private NotificationMessage produce(NotificationMessage.Type type, long sourceId, String content) {
        return new NotificationMessage(type, this.userService.getUserOutline(sourceId), content);
    }

    private String articleSummary(Article article) {
        final String content = article.getContent();
        return (content.length() > 10) ? content.substring(0, 7) + "..." : content;
    }
}