package techcourse.fakebook.service.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationChannelRepository notificationChannelRepository;

    public NotificationService(NotificationChannelRepository notificationChannelRepository) {
        this.notificationChannelRepository = notificationChannelRepository;
    }

    public String issue(long id) {
        return this.notificationChannelRepository.assignTo(id);
    }
}