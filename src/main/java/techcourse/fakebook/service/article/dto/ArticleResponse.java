package techcourse.fakebook.service.article.dto;

import techcourse.fakebook.service.attachment.dto.AttachmentResponse;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleResponse {
    private Long id;
    private String content;
    private LocalDateTime recentDate;
    private UserOutline userOutline;
    private List<AttachmentResponse> attachments;

    private ArticleResponse() {
    }

    public ArticleResponse(Long id, String content, LocalDateTime recentDate, UserOutline userOutline) {
        this.id = id;
        this.content = content;
        this.recentDate = recentDate;
        this.userOutline = userOutline;
    }

    public ArticleResponse(Long id, String content, LocalDateTime recentDate, UserOutline userOutline, List<AttachmentResponse> attachments) {
        this.id = id;
        this.content = content;
        this.recentDate = recentDate;
        this.userOutline = userOutline;
        this.attachments = attachments;
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

    public List<AttachmentResponse> getAttachments() {
        return attachments;
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

    public void setAttachments(List<AttachmentResponse> attachments) {
        this.attachments = attachments;
    }
}
