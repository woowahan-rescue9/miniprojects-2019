package techcourse.fakebook.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.LoginRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

public class CommentApiControllerTest extends ControllerTestHelper {
    @LocalServerPort
    private int port;

    private LoginRequest loginRequest = new LoginRequest("van@van.com", "Password!1");
    private String cookie;

    @BeforeEach
    void setUp() {
        cookie = getCookie(login(loginRequest));
    }

    @Test
    void 댓글을_잘_불러오는지_확인한다() {
        given().
                port(port).
        when().
                get("/articles/1/comments").
        then().
                statusCode(200).
                body(containsString("댓글입니다."));
    }

    @Test
    void 댓글을_잘_작성하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("hello");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
                body(commentRequest).
        when().
                post("/articles/1/comments").
        then().
                statusCode(201).
                body("content", equalTo(commentRequest.getContent()));
    }

    @Test
    void 댓글을_잘_삭제하는지_확인한다() {
        given().
                port(port).
                cookie(cookie).
        when().
                delete("/articles/1/comments/2").
        then().
                statusCode(204);
    }

    @Test
    void 댓글을_잘_수정하는지_확인() {
        CommentRequest commentRequest = new CommentRequest("수정된 댓글입니다.");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
                body(commentRequest).
        when().
                put("/articles/1/comments/1").
        then().
                statusCode(200).
                body("content", equalTo(commentRequest.getContent()));
    }
}
