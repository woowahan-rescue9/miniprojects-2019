package techcourse.fakebook.service.dto;

import java.time.LocalDateTime;

public class ArticleResponse {
    private Long id;
    private String content;
    private LocalDateTime recentDate;
    private UserOutline userOutline;

    private ArticleResponse() {
    }

    public ArticleResponse(Long id, String content, LocalDateTime recentDate, UserOutline userOutline) {
        this.id = id;
        this.content = content;
        this.recentDate = recentDate;
        this.userOutline = userOutline;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getRecentDate() {
        return recentDate;
    }

    public UserOutline getUserOutline() {
        return userOutline;
    }
}
