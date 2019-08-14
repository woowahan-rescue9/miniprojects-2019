package techcourse.fakebook.service.dto;

import techcourse.fakebook.controller.utils.SessionUser;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private SessionUser sessionUser;

    public CommentResponse(Long id, String content, LocalDateTime createdDate, SessionUser sessionUser) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.sessionUser = sessionUser;
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

    public SessionUser getSessionUser() {
        return sessionUser;
    }
}
