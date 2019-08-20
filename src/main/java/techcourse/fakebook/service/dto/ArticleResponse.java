package techcourse.fakebook.service.dto;

import techcourse.fakebook.domain.article.ArticleMultipart;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleResponse {
    private Long id;
    private String content;
    private LocalDateTime recentDate;
    private UserOutline userOutline;
    private List<ArticleMultipart> resources;

    private ArticleResponse() {
    }

    public ArticleResponse(Long id, String content, LocalDateTime recentDate, UserOutline userOutline) {
        this.id = id;
        this.content = content;
        this.recentDate = recentDate;
        this.userOutline = userOutline;
    }

    public ArticleResponse(Long id, String content, LocalDateTime recentDate, UserOutline userOutline, List<ArticleMultipart> resources) {
        this.id = id;
        this.content = content;
        this.recentDate = recentDate;
        this.userOutline = userOutline;
        this.resources = resources;
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

    public List<ArticleMultipart> getResources() {
        return resources;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRecentDate(LocalDateTime recentDate) {
        this.recentDate = recentDate;
    }

    public void setUserOutline(UserOutline userOutline) {
        this.userOutline = userOutline;
    }

    public void setResources(List<ArticleMultipart> resources) {
        this.resources = resources;
    }
}
