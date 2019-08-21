package techcourse.fakebook.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.LoginRequest;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
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
    void 글_목록을_잘_불러오는지_확인한다() {
        writeArticle();

        List<ArticleResponse> articles = given().
                port(port).
        when().
                get("/api/articles").
        then().
                statusCode(200).
                extract().
                body().
                jsonPath().getList(".", ArticleResponse.class);

        assertThat(articles.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void 글을_잘_작성하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("hello");

        given().
                port(port).
                cookie(cookie).
                formParam("content", "hello").
        when().
                post("/api/articles").
        then().
                statusCode(201).
                body("content", equalTo(articleRequest.getContent()));
    }

    @Test
    void 글을_잘_삭제하는지_확인한다() {
        given().
                port(port).
                cookie(cookie).
        when().
                delete("/api/articles/2").
        then().
                statusCode(204);
    }

    @Test
    void 글을_잘_수정하는지_확인한다() {
        ArticleRequest articleRequest = new ArticleRequest("수정된 글입니다.");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
                body(articleRequest).
        when().
                put("/api/articles/1").
        then().
                statusCode(200).
                body("content", equalTo(articleRequest.getContent()));
    }

    @Test
    void 좋아요_여부를_확인한다() {
        ArticleResponse article = writeArticle();

        given().
                port(port).
                cookie(cookie).
        when().
                get("api/articles/" + article.getId() + "/like").
        then().
                statusCode(204);
    }

    @Test
    void 좋아요가_잘_등록되는지_확인한다() {
        ArticleResponse article = writeArticle();

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/articles/" + article.getId() + "/like").
        then().
                statusCode(201);
    }

    @Test
    void 좋아요가_잘_삭제되는지_확인한다() {
        ArticleResponse article = writeArticle();

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/articles/" + article.getId() + "/like").
        then().
                statusCode(201);

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/articles/" + article.getId() + "/like").
        then().
                statusCode(204);
    }

    @Test
    void 좋아요_개수를_잘_불러오는지_확인한다() {
        ArticleResponse articleResponse = writeArticle();

        //좋아요를 누른다.
        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
        when().
                post("/api/articles/" + articleResponse.getId() + "/like").
        then().
                statusCode(201);

        given().
                port(port).
                cookie(cookie).
        when().
                get("/api/articles/" + articleResponse.getId() + "/like/count").
        then().
                statusCode(200).
                body(equalTo("1"));
    }

    @Test
    void 게시글_이미지_포함_업로드가_잘_되는지_확인한다() {
        ArticleResponse articleResponse = given().
                port(port).
                cookie(cookie).
                multiPart("files", new File("src/test/resources/static/images/logo/res9-logo.gif")).
                formParam("content","hello").
        when().
                post("/api/articles").as(ArticleResponse.class);

        assertThat(articleResponse.getResources().size()).isEqualTo(1);
    }
}