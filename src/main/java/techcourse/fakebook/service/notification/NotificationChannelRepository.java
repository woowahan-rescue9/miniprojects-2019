package techcourse.fakebook.service.notification;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationChannelRepository {
    private final Map<Long, String> channels = new ConcurrentHashMap<>();

    public Optional<String> retrieveAddressOf(long id) {
        return Optional.ofNullable(this.channels.get(id));
    }

    public String assignTo(long id) {
        return this.channels.put(id, UUID.randomUUID().toString().replaceAll("-", ""));
    }

    public void resetAll() {
        this.channels.clear();
    }
}