package techcourse.fakebook.web.controller.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.article.dto.ArticleResponse;
import techcourse.fakebook.service.comment.dto.CommentRequest;
import techcourse.fakebook.service.comment.dto.CommentResponse;
import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.web.controller.ControllerTestHelper;

import static io.restassured.RestAssured.given;
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
                statusCode(HttpStatus.OK.value()).
                body("size", greaterThanOrEqualTo(2));
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
                statusCode(HttpStatus.CREATED.value()).
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
                statusCode(HttpStatus.NO_CONTENT.value());
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
                statusCode(HttpStatus.OK.value()).
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
                statusCode(HttpStatus.NO_CONTENT.value());
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
                statusCode(HttpStatus.CREATED.value());
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
                statusCode(HttpStatus.CREATED.value());

        given().
                port(port).
                cookie(cookie).
        when().
                post("/api/comments/" + comment.getId() + "/like").
        then().
                statusCode(HttpStatus.NO_CONTENT.value());
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
                statusCode(HttpStatus.CREATED.value());

        given().
                port(port).
                cookie(cookie).
        when().
                get("/api/comments/" + commentResponse.getId() + "/like/count").
        then().
                statusCode(HttpStatus.OK.value()).
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
                statusCode(HttpStatus.OK.value()).
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