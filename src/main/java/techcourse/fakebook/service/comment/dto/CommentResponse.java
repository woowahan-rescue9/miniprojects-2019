package techcourse.fakebook.service.comment.dto;

import techcourse.fakebook.service.user.dto.UserOutline;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private UserOutline userOutline;

    private CommentResponse() {
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setUserOutline(UserOutline userOutline) {
        this.userOutline = userOutline;
    }
}
