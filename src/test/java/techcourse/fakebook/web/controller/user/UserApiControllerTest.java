package techcourse.fakebook.web.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.service.user.dto.UserUpdateRequest;
import techcourse.fakebook.web.controller.ControllerTestHelper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

class UserApiControllerTest extends ControllerTestHelper {
    @LocalServerPort
    private int port;

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
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(sessionId).
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

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("updatedCoverUrl", "updatedIntroduction");

        given().
                port(port).
                sessionId(sessionId).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                accept(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(userUpdateRequest).
        when().
                put("/api/users/" + userId).
        then().
                statusCode(HttpStatus.OK.value()).
                body("coverUrl", equalTo(userUpdateRequest.getCoverUrl())).
                body("introduction", equalTo(userUpdateRequest.getIntroduction()));
    }

    @Test
    void 비로그인_존재하는_유저_수정() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        signup(userSignupRequest);
        Long userId = getId(userSignupRequest.getEmail());

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("updatedCoverUrl", "updatedIntroduction");

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