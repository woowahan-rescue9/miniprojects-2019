package techcourse.fakebook.service.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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

    public NotificationMessage chatFrom(long sourceId, String content) {
        return this.notificationMessageFactory.chat(sourceId, content);
    }

    public NotificationMessage friendRequestFrom(long sourceId) {
        return this.notificationMessageFactory.friendRequest(sourceId);
    }

    public NotificationMessage commentFrom(long sourceId) {
        return this.notificationMessageFactory.comment(sourceId);
    }

    public NotificationMessage likeFrom(long sourceId) {
        return this.notificationMessageFactory.like(sourceId);
    }

    public void closeChannelOf(long id) {
        this.notificationChannelRepository.resetBy(id);
    }

    public Optional<NotificationChannel> _getChannelOf(long id) {
        return this.notificationChannelRepository.retrieveBy(id);
    }
}