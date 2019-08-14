package techcourse.fakebook.service.dto;

import techcourse.fakebook.controller.utils.SessionUser;

public class ArticleResponse {
    private Long id;
    private String content;
    private SessionUser sessionUser;

    public ArticleResponse(Long id, String content, SessionUser sessionUser) {
        this.id = id;
        this.content = content;
        this.sessionUser = sessionUser;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public SessionUser getSessionUser() {
        return sessionUser;
    }
}
