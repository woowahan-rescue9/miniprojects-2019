package techcourse.fakebook.service.dto;

public class ArticleResponse {
    private Long id;
    private String content;
    private UserOutline userOutline;

    private ArticleResponse() {}

    public ArticleResponse(Long id, String content, UserOutline userOutline) {
        this.id = id;
        this.content = content;
        this.userOutline = userOutline;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public UserOutline getUserOutline() {
        return userOutline;
    }
}
