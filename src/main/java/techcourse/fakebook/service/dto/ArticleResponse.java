package techcourse.fakebook.service.dto;

public class ArticleResponse {
    private Long id;
    private String content;
    private UserDto userDto;

    public ArticleResponse(Long id, String content, UserDto userDto) {
        this.id = id;
        this.content = content;
        this.userDto = userDto;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public UserDto getUserDto() {
        return userDto;
    }
}
