package techcourse.fakebook.controller;

import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.service.dto.LoginRequest;

import javax.xml.ws.Response;

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
                get("/api/articles/1/comments").
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
                post("/api/articles/1/comments").
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
                delete("/api/articles/1/comments/2").
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
                put("/api/articles/1/comments/1").
        then().
                statusCode(200).
                body("content", equalTo(commentRequest.getContent()));
    }

    @Test
    void 좋아요_확인_테스트() {
        CommentResponse comment = writeComment();

        given().
                port(port).
                cookie(cookie).
        when().
                get("/api/articles/1/comments/" + comment.getId() +"/like").
        then().
                statusCode(204);
    }

    @Test
    void 좋아요_등록_테스트() {
        CommentResponse comment = writeComment();

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/articles/1/comments/" + comment.getId() +"/like").
        then().
                statusCode(201);
    }

    @Test
    void 좋아요_삭제_테스트() {
        CommentResponse comment = writeComment();

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/articles/1/comments/" + comment.getId() +"/like").
        then().
                statusCode(201);

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/articles/1/comments/" + comment.getId() +"/like").
        then().
                statusCode(204);
    }


    private CommentResponse writeComment() {
        CommentRequest commentRequest = new CommentRequest("hello");

        return given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
                body(commentRequest).
        when().
                post("/api/articles/1/comments").as(CommentResponse.class);
    }
}
