package techcourse.fakebook.service.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;

    public CommentResponse(Long id, String content, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
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
}
