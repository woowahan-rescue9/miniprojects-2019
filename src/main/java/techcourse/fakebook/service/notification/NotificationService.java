package techcourse.fakebook.service.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.service.notification.assembler.NotificationAssembler;
import techcourse.fakebook.service.notification.dto.NotificationResponse;

@Service
public class NotificationService {
    private final NotificationChannelMapper notificationChannelMapper;
    private final NotificationAssembler notificationAssembler;
    private final SimpMessagingTemplate messenger;

    public NotificationService(
            NotificationChannelMapper notificationChannelMapper,
            NotificationAssembler notificationAssembler,
            SimpMessagingTemplate messenger
    ) {
        this.notificationChannelMapper = notificationChannelMapper;
        this.notificationAssembler = notificationAssembler;
        this.messenger = messenger;
    }

    public NotificationChannel issueNewChannelTo(long userId) {
        return this.notificationChannelMapper.assignTo(userId);
    }

    public void chatFromTo(long srcUserId, long destUserId, String content) {
        notifyTo(destUserId, this.notificationAssembler.chat(srcUserId, content));
    }

    public void friendRequestFromTo(long srcUserId, long destUserId) {
        notifyTo(destUserId, this.notificationAssembler.friendRequest(srcUserId));
    }

    public void commentFromTo(long srcUserId, Article destArticle, Comment comment) {
        notifyTo(destArticle.getUser().getId(), this.notificationAssembler.comment(srcUserId, destArticle, comment));
    }

    public void likeFromTo(long srcUserId, Article destArticle) {
        notifyTo(destArticle.getUser().getId(), this.notificationAssembler.like(srcUserId, destArticle));
    }

    private void notifyTo(long destUserId, NotificationResponse message) {
        this.notificationChannelMapper.retrieveBy(destUserId).ifPresent(channel ->
                this.messenger.convertAndSend(
                        NotificationConfig.MESSAGE_BROKER_URI + "/" + channel.getAddress(),
                        message
                )
        );
    }

    public void closeChannelOf(long userId) {
        this.notificationChannelMapper.resetBy(userId);
    }
}