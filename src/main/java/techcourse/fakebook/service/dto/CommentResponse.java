package techcourse.fakebook.service.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private UserOutline userOutline;

    public CommentResponse(Long id, String content, LocalDateTime createdDate, UserOutline userOutline) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.userOutline = userOutline;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public UserOutline getUserOutline() {
        return userOutline;
    }
}
