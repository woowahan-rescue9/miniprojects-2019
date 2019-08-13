package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.ArticleRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleApiControllerTest {
    @LocalServerPort
    private int port;

    @Test
    void 글을_잘_작성하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("hello");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(articleRequest).
        when().
                post("/articles").
        then().
                statusCode(201).
                body("content", equalTo(articleRequest.getContent()));
    }

    @Test
    void 글을_잘_삭제하는지_확인() {
        given().
                port(port).
        when().
                delete("/articles/2").
        then().
                statusCode(204);
    }

    @Test
    void 글을_잘_수정하는지_확인() {
        ArticleRequest articleRequest = new ArticleRequest("수정된 글입니다.");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(articleRequest).
        when().
                put("/articles/1").
        then().
                statusCode(200).
                body("content", equalTo(articleRequest.getContent()));
    }
}
