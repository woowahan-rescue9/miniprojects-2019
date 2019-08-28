package techcourse.fakebook.service.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;

@Service
public class NotificationService {
    private final NotificationChannelRepository notificationChannelRepository;
    private final NotificationMessageFactory notificationMessageFactory;
    private final SimpMessagingTemplate messenger;

    public NotificationService(
            NotificationChannelRepository notificationChannelRepository,
            NotificationMessageFactory notificationMessageFactory,
            SimpMessagingTemplate messenger
    ) {
        this.notificationChannelRepository = notificationChannelRepository;
        this.notificationMessageFactory = notificationMessageFactory;
        this.messenger = messenger;
    }

    public NotificationChannel issueNewChannelTo(long userId) {
        return this.notificationChannelRepository.assignTo(userId);
    }

    public void notifyTo(long destUserId, NotificationMessage message) {
        this.notificationChannelRepository.retrieveBy(destUserId).ifPresent(channel ->
            this.messenger.convertAndSend(WebSocketConfig.MESSAGE_BROKER_URI + "/" + channel, message)
        );
    }

    public NotificationMessage writeChatMessageFrom(long srcUserId, String content) {
        return this.notificationMessageFactory.chat(srcUserId, content);
    }

    public NotificationMessage writeFriendRequestMessageFrom(long srcUserId) {
        return this.notificationMessageFactory.friendRequest(srcUserId);
    }

    public NotificationMessage writeCommentMessageFrom(long srcUserId, Article srcArticle) {
        return this.notificationMessageFactory.comment(srcUserId, srcArticle);
    }

    public NotificationMessage writeLikeMessageFrom(long srcUserId, Article srcArticle) {
        return this.notificationMessageFactory.like(srcUserId, srcArticle);
    }

    public void closeChannelOf(long userId) {
        this.notificationChannelRepository.resetBy(userId);
    }
}