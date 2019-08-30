package techcourse.fakebook.service.chat.dto;

public class ChatResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String content;
    private Boolean read;

    private ChatResponse() {
    }

    public ChatResponse(Long id, Long userId, String userName, String content, Boolean read) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public Long getUserId() {
        return userId;
    }

    public Boolean getRead() {
        return read;
    }
}
