package techcourse.fakebook.service.article.dto;

import techcourse.fakebook.service.attachment.dto.AttachmentResponse;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private UserOutline userOutline;
    private List<AttachmentResponse> attachments;

    private ArticleResponse() {
    }

    public ArticleResponse(Long id, String content, LocalDateTime createdDate, UserOutline userOutline) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.userOutline = userOutline;
    }

    public ArticleResponse(Long id, String content, LocalDateTime createdDate, UserOutline userOutline, List<AttachmentResponse> attachments) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.userOutline = userOutline;
        this.attachments = attachments;
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

    public List<AttachmentResponse> getAttachments() {
        return attachments;
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

    public void setAttachments(List<AttachmentResponse> attachments) {
        this.attachments = attachments;
    }
}
