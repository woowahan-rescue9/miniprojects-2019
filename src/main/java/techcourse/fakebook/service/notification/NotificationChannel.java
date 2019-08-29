package techcourse.fakebook.service.notification;

import java.util.Objects;
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
        return "{ " + this.address + " } : NotificationChannel";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationChannel)) {
            return false;
        }
        final NotificationChannel rhs = (NotificationChannel) o;
        return Objects.equals(this.address, rhs.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.address);
    }
}