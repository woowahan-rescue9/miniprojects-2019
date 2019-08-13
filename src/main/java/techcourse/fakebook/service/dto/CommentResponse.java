package techcourse.fakebook.service.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private UserDto userDto;

    public CommentResponse(Long id, String content, LocalDateTime createdDate, UserDto userDto) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.userDto = userDto;
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

    public UserDto getUserDto() {
        return userDto;
    }
}
