package techcourse.fakebook.service.chat.dto;

public class ChatRequest {
    private Long userId;
    private String content;

    private ChatRequest() {
    }

    public ChatRequest(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }
}
