package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.CommentRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentApiControllerTest {
    @LocalServerPort
    private int port;

    @Test
    void 댓글을_잘_작성하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("hello");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
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
                body(commentRequest).
        when().
                put("/articles/1/comments/1").
        then().
                statusCode(200).
                body("content", equalTo(commentRequest.getContent()));
    }
}
