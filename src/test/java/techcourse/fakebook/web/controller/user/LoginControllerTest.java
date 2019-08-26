package techcourse.fakebook.web.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.web.controller.ControllerTestHelper;

import static io.restassured.RestAssured.given;

class LoginControllerTest extends ControllerTestHelper {
    @LocalServerPort
    private int port;

    @Test
    void 로그인_성공() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();

        signup(userSignupRequest)
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 로그인_후_로그인_시도() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String sessionId = getSessionId(signup(userSignupRequest));

        LoginRequest loginRequest = new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword());

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(sessionId).
                body(loginRequest).
        when().
                post("/api/login").
        then().
                statusCode(HttpStatus.FOUND.value());
    }

    @Test
    void 로그아웃_성공() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String cookie = getSessionId(signup(userSignupRequest));

        LoginRequest loginRequest = new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword());

        webTestClient.get().uri("/logout")
                .cookie("JSESSIONID", cookie)
                .exchange()
                .expectStatus()
                .isFound().expectHeader().
                valueMatches("location", ".*/");
    }

    @Test
    void 비로그인_로그아웃_시도() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound().expectHeader().valueMatches("location", ".*/");;
    }
}