package techcourse.fakebook.service.dto;

public class ArticleResponse {
    private Long id;
    private String content;

    public ArticleResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
