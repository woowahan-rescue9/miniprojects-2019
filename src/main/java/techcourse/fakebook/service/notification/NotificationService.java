package techcourse.fakebook.service.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;

import java.util.Optional;

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

    public NotificationChannel issueNewChannelTo(long id) {
        return this.notificationChannelRepository.assignTo(id);
    }

    public void notifyTo(long id, NotificationMessage message) {
        this.notificationChannelRepository.retrieveBy(id).ifPresent(channel ->
            this.messenger.convertAndSend("/api/notification/" + channel, message)
        );
    }

    public NotificationMessage writeChatMessageFrom(long sourceId, String content) {
        return this.notificationMessageFactory.chat(sourceId, content);
    }

    public NotificationMessage writeFriendRequestMessageFrom(long sourceId) {
        return this.notificationMessageFactory.friendRequest(sourceId);
    }

    public NotificationMessage writeCommentMessageFrom(long sourceId, Article sourceArticle) {
        return this.notificationMessageFactory.comment(sourceId, sourceArticle);
    }

    public NotificationMessage writeLikeMessageFrom(long sourceId, Article sourceArticle) {
        return this.notificationMessageFactory.like(sourceId, sourceArticle);
    }

    public void closeChannelOf(long id) {
        this.notificationChannelRepository.resetBy(id);
    }

    public Optional<NotificationChannel> _getChannelOf(long id) {
        return this.notificationChannelRepository.retrieveBy(id);
    }
}