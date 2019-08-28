package techcourse.fakebook.service.notification;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationChannelRepository {
    private final Map<Long, NotificationChannel> channels = new ConcurrentHashMap<>();

    public NotificationChannel assignTo(long id) {
        final NotificationChannel channel = new NotificationChannel();
        this.channels.put(id, channel);
        return channel;
    }

    public Optional<NotificationChannel> retrieveBy(long id) {
        return Optional.ofNullable(this.channels.get(id));
    }

    public void resetBy(long id) {
        this.channels.remove(id);
    }
}