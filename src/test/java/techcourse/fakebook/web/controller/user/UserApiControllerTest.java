package techcourse.fakebook.web.controller.user;

import io.restassured.internal.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.service.user.dto.UserUpdateRequest;
import techcourse.fakebook.web.controller.ControllerTestHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class UserApiControllerTest extends ControllerTestHelper {
    @LocalServerPort
    private int port;
    private MultipartFile image;

    @BeforeEach
    void setUp() throws IOException {
        File file = new File("src/test/resources/static/images/user/profile/default.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile image = new MockMultipartFile("file", file.getName(), "image/gif", IOUtils.toByteArray(input));
    }

    @Test
    void 유저_아이디로_유저정보_조회() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String sessionId = getSessionId(signup(userSignupRequest));
        login(new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword()));

        given().
                port(port).
                sessionId(sessionId).
        when().
                get("/api/users/1/info").
        then().
                statusCode(HttpStatus.OK.value()).
                body("id", equalTo(1)).
                body("introduction", equalTo("introduction"));
    }

    @Test
    void 유저생성_올바른_입력() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();

        signup(userSignupRequest).
                statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 유저생성_존재하는_이메일() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();

        signup(userSignupRequest);

        signup(userSignupRequest).
                statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 유저생성_존재하는_틀린_이메일_양식() {
        UserSignupRequest userSignupRequest = new UserSignupRequest("email",
                "abc",
                "abc",
                "1q2w3e$R",
                "male",
                "123456");
        signup(userSignupRequest).
                statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 유저생성_존재하는_틀린_이름_양식() {
        UserSignupRequest userSignupRequest = new UserSignupRequest("abc@b.c",
                "abc",
                "!",
                "1q2w3e$R",
                "male",
                "123456");
        signup(userSignupRequest).
                statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 유저생성_존재하는_틀린_비밀번호_양식() {
        UserSignupRequest userSignupRequest = new UserSignupRequest("abc@b.c",
                "abc",
                "a",
                "1q2w3e4r",
                "male",
                "123456");
        signup(userSignupRequest).
                statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 로그인된_유저생성_올바른_입력() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String sessionId = getSessionId(signup(userSignupRequest));
        login(new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword()));

        UserSignupRequest otherUserSignupRequest = newUserSignupRequest();

        given().
                port(port).
                sessionId(sessionId).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(otherUserSignupRequest).
        when().
                post("/api/users").
        then().
                statusCode(HttpStatus.FOUND.value());
    }

    @Test
    void 로그인_존재하는_유저_수정() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String sessionId = getSessionId(signup(userSignupRequest));
        Long userId = getId(userSignupRequest.getEmail());

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(image,"updatedIntroduction",
                "updatedName", "!234Qwer");
        given().
                port(port).
                sessionId(sessionId).
                multiPart("profileImage", new File("src/test/resources/static/images/user/profile/default.png")).
                formParam("introduction", "updatedIntroduction").
                formParam("name", "updatedName").
                formParam("password", "!234Qwer").
        when().
                put("/api/users/" + userId).
        then().
                statusCode(HttpStatus.OK.value()).
                body("introduction", equalTo(userUpdateRequest.getIntroduction())).
                body("name", equalTo(userUpdateRequest.getName()));
    }

    @Test
    void 비로그인_존재하는_유저_수정() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        signup(userSignupRequest);
        Long userId = getId(userSignupRequest.getEmail());

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(image, "updatedIntroduction",
                "updatedName", "!234Qwer");
        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                accept(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(userUpdateRequest).
        when().
                put("/api/users/" + userId).
        then().
                statusCode(HttpStatus.FOUND.value());
    }

    @Test
    void 유저가_작성한_글_조회() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String sessionId = getSessionId(signup(userSignupRequest));
        login(new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword()));

        writeArticle();

        given().
                port(port).
                sessionId(sessionId).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
        when().
                get("/api/users/1/articles").
        then().
                statusCode(HttpStatus.OK.value()).
                body("size", greaterThanOrEqualTo(1)).
                body("articleResponse.content", hasItem("hello"));
    }

    @Test
    void 로그인_키워드로_유저이름_조회() {
        UserSignupRequest userSignupRequest =
                new UserSignupRequest("aa@bb.cc", "keyword", "qwe", "1q2w3e$R", "M", "123456");

        String sessionId = getSessionId(signup(userSignupRequest));

        given().
                port(port).
                sessionId(sessionId).
                accept(MediaType.APPLICATION_JSON_UTF8_VALUE).
        when().
                get("/api/users/" + "keyword").
        then().
                statusCode(HttpStatus.OK.value()).
                body("name", hasItem("keywordqwe"));
    }
}