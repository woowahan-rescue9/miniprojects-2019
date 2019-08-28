package techcourse.fakebook.service.notification;

import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.Objects;

public class NotificationMessage {
    public enum Type {
        CHAT,
        FRIEND_REQUEST,
        COMMENT,
        LIKE
    }

    private final Type type;
    private final UserOutline srcUser;
    private final String content;

    public NotificationMessage(Type type, UserOutline srcUser, String content) {
        this.type = type;
        this.srcUser = srcUser;
        this.content = content;
    }

    public Type getType() {
        return this.type;
    }

    public UserOutline getSrcUser() {
        return this.srcUser;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return "{ " +
                    "type: " + this.type + ", " +
                    "sourceId: " + this.srcUser + ", " +
                    " content: " + this.content +
                " } : NotificationMessage";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationMessage)) {
            return false;
        }
        final NotificationMessage rhs = (NotificationMessage) o;
        return this.type == rhs.type &&
                Objects.equals(this.srcUser, rhs.srcUser) &&
                Objects.equals(this.content, rhs.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.srcUser, this.content);
    }
}