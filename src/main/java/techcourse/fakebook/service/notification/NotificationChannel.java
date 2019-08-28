package techcourse.fakebook.service.notification;

import java.util.UUID;

public class NotificationChannel {
    private String address;

    public NotificationChannel() {
        this.address = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return this.address;
    }
}