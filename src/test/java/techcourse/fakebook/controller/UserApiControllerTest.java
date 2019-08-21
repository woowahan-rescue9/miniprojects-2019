package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.UserSignupRequest;
import techcourse.fakebook.service.dto.UserUpdateRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

class UserApiControllerTest extends ControllerTestHelper {
    @LocalServerPort
    private int port;

    @Test
    void 로그인_존재하는_유저_수정() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String cookie = getCookie(signup(userSignupRequest));
        Long userId = getId(userSignupRequest.getEmail());

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("updatedCoverUrl", "updatedIntroduction");

        given().
                port(port).
                cookie(cookie).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                accept(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(userUpdateRequest).
        when().
                put("/api/users/" + userId).
        then().
                statusCode(200).
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
                statusCode(302);
    }

    @Test
    void 로그인_키워드로_유저이름_조회() {
        UserSignupRequest userSignupRequest =
                new UserSignupRequest("aa@bb.cc", "keyword", "123", "1q2w3e$R", "M", "123456");
        String cookie = getCookie(signup(userSignupRequest));

                given().
                        port(port).
                        cookie(cookie).
                        accept(MediaType.APPLICATION_JSON_UTF8_VALUE).
                when().
                        get("/api/users/" + "keyword").
                then().
                        statusCode(200).
                        body(".", hasItem("keyword123"));
    }
}