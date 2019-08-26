package techcourse.fakebook.service.article.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ArticleRequest {
    private String content;

    private List<MultipartFile> files;

    public ArticleRequest() {
    }

    public ArticleRequest(String content) {
        this.content = content;
    }

    public ArticleRequest(String content, List<MultipartFile> files) {
        this.content = content;
        this.files = files;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public String getContent() {
        return content;
    }
}
