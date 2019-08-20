package techcourse.fakebook.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.service.dto.LoginRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
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
                delete("/api/comments/2").
        then().
                statusCode(204);
    }

    @Test
    void 댓글을_잘_수정하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("수정된 댓글입니다.");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
                body(commentRequest).
        when().
                put("/api/comments/1").
        then().
                statusCode(200).
                body("content", equalTo(commentRequest.getContent()));
    }

    @Test
    void 좋아요_여부를_확인한다() {
        CommentResponse comment = writeComment();

        given().
                port(port).
                cookie(cookie).
        when().
                get("/api/comments/" + comment.getId() + "/like").
        then().
                statusCode(204);
    }

    @Test
    void 좋아요가_잘_등록되는지_확인한다() {
        CommentResponse comment = writeComment();

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/comments/" + comment.getId() + "/like").
        then().
                statusCode(201);
    }

    @Test
    void 좋아요가_잘_삭제되는지_확인한다() {
        CommentResponse comment = writeComment();

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/comments/" + comment.getId() + "/like").
        then().
                statusCode(201);

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/comments/" + comment.getId() + "/like").
        then().
                statusCode(204);
    }

    @Test
    void 좋아요_개수를_잘_불러오는지_확인한다() {
        CommentResponse commentResponse = writeComment();

        //좋아요 등록
        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/comments/" + commentResponse.getId() + "/like").
        then().
                statusCode(201);


        given().
                port(port).
                cookie(cookie).
        when().
                get("/api/comments/" + commentResponse.getId() + "/like/count").
        then().
                statusCode(200).
                body(equalTo("1"));
    }

    @Test
    void 게시글에_따른_댓글_개수를_잘_불러오는지_확인한다() {
        ArticleResponse articleResponse = writeArticle();
        writeComment(articleResponse.getId());

        given().
                port(port).
        when().
                get("/api/articles/" + articleResponse.getId() + "/comments/count").
        then().
                statusCode(200).
                body(equalTo("1"));
    }

    private CommentResponse writeComment() {
        return writeComment(1L);
    }

    private CommentResponse writeComment(Long articleId) {
        CommentRequest commentRequest = new CommentRequest("hello");

        return given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
                body(commentRequest).
        when().
                post("/api/articles/" + articleId + "/comments").as(CommentResponse.class);
    }
}