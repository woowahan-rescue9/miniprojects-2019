package techcourse.fakebook.service.dto;

public class ArticleRequest {
    private String content;

    public ArticleRequest() {}

    public ArticleRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}