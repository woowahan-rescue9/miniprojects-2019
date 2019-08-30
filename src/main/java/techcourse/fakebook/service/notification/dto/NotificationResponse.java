package techcourse.fakebook.service.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    public enum Type {
        CHAT,
        FRIEND_REQUEST,
        COMMENT,
        LIKE
    }

    private final Type type;
    private final UserOutline srcUser;
    private final String content;

    public NotificationResponse(Type type, UserOutline srcUser, String content) {
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
                " } : NotificationResponse";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationResponse)) {
            return false;
        }
        final NotificationResponse rhs = (NotificationResponse) o;
        return this.type == rhs.type &&
                Objects.equals(this.srcUser, rhs.srcUser) &&
                Objects.equals(this.content, rhs.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.srcUser, this.content);
    }
}