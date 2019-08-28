package techcourse.fakebook.service.notification;

import techcourse.fakebook.service.user.dto.UserOutline;

public class NotificationMessage {
    public enum Type {
        CHAT,
        FRIEND_REQUEST,
        COMMENT,
        LIKE
    }

    private final Type type;
    private final UserOutline sourceUser;
    private final String content;

    public NotificationMessage(Type type, UserOutline sourceUser, String content) {
        this.type = type;
        this.sourceUser = sourceUser;
        this.content = content;
    }

    public Type getType() {
        return this.type;
    }

    public UserOutline getSourceUser() {
        return this.sourceUser;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return "{ " +
                    "type: " + this.type + ", " +
                    "sourceId: " + this.sourceUser + ", " +
                    " content: " + this.content +
                " } : NotificationMessage";
    }
}