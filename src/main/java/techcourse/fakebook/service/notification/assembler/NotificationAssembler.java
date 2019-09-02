package techcourse.fakebook.service.notification.assembler;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.service.notification.dto.NotificationResponse;
import techcourse.fakebook.service.user.UserService;

@Component
public class NotificationAssembler {
    public int MAX_SUMMARY_LENGTH = 20;
    private final UserService userService;

    public NotificationAssembler(UserService userService) {
        this.userService = userService;
    }

    public NotificationResponse chat(long srcUserId, String content) {
        return toResponse(NotificationResponse.Type.CHAT, srcUserId, null, content);
    }

    public NotificationResponse friendRequest(long srcUserId) {
        return toResponse(NotificationResponse.Type.FRIEND_REQUEST, srcUserId, null, null);
    }

    public NotificationResponse comment(Comment comment, long srcUserId, Article destArticle) {
        return toResponse(NotificationResponse.Type.COMMENT, srcUserId, summarize(destArticle), comment.getContent());
    }

    public NotificationResponse like(long srcUserId, Article destArticle) {
        return toResponse(NotificationResponse.Type.LIKE, srcUserId, summarize(destArticle), null);
    }

    private NotificationResponse toResponse(
            NotificationResponse.Type type,
            long srcUserId,
            String srcSummary,
            String content
    ) {
        return new NotificationResponse(type, this.userService.getUserOutline(srcUserId), srcSummary, content);
    }

    private String summarize(String content) {
        return (content.length() > this.MAX_SUMMARY_LENGTH)
                ? content.substring(0, this.MAX_SUMMARY_LENGTH - 4) + " ..."
                : content;
    }

    private String summarize(Article article) {
        return summarize(article.getContent());
    }
}