package techcourse.fakebook.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.LoginRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ArticleApiControllerTest extends ControllerTestHelper {
    @LocalServerPort
    private int port;

    private LoginRequest loginRequest = new LoginRequest("van@van.com", "Password!1");
    private String cookie;

    @BeforeEach
    void setUp() {
        cookie = getCookie(login(loginRequest));
    }

    @Test
    void 글을_잘_작성하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("hello");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
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
                cookie(cookie).
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
                cookie(cookie).
                body(articleRequest).
        when().
                put("/articles/1").
        then().
                statusCode(200).
                body("content", equalTo(articleRequest.getContent()));
    }
}
