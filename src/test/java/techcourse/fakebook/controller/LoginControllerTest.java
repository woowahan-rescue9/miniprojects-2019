package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.fakebook.service.dto.LoginRequest;
import techcourse.fakebook.service.dto.UserSignupRequest;

class LoginControllerTest extends ControllerTestHelper {

    @Test
    void 로그인_성공() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();

        signup(userSignupRequest)
                .expectStatus()
                .isFound().expectHeader().valueMatches("location", ".*/newsfeed.*");
    }

    @Test
    void 로그인_후_로그인_시도() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String cookie = getCookie(signup(userSignupRequest));

        LoginRequest loginRequest = new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword());

        webTestClient.post().uri("/login")
                .header("Cookie", cookie)
                .body(BodyInserters.fromFormData("email", loginRequest.getEmail())
                        .with("password", loginRequest.getPassword())
                )
                .exchange()
                .expectStatus()
                .isFound().expectHeader().valueMatches("location", ".*/newsfeed");
    }

    @Test
    void 로그아웃_성공() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String cookie = getCookie(signup(userSignupRequest));

        LoginRequest loginRequest = new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword());

        webTestClient.get().uri("/logout")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound().expectHeader().valueMatches("location", ".*/");
    }

    @Test
    void 비로그인_로그아웃_시도() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound().expectHeader().valueMatches("location", ".*/");;
    }
}