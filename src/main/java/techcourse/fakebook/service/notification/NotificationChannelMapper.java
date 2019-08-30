package techcourse.fakebook.service.notification;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationChannelMapper {
    private final Map<Long, NotificationChannel> channels = new ConcurrentHashMap<>();

    public NotificationChannel assignTo(long userId) {
        final NotificationChannel channel = new NotificationChannel();
        this.channels.put(userId, channel);
        return channel;
    }

    public Optional<NotificationChannel> retrieveBy(long userId) {
        return Optional.ofNullable(this.channels.get(userId));
    }

    public void resetBy(long userId) {
        this.channels.remove(userId);
    }
}