package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.fakebook.service.dto.LoginRequest;
import techcourse.fakebook.service.dto.UserSignupRequest;

class LoginControllerTest extends ControllerTestHelper {

    @Test
    void 로그인_성공() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        signup(userSignupRequest);

        LoginRequest loginRequest = new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword());

        webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", loginRequest.getEmail())
                        .with("password", loginRequest.getPassword())
                )
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 로그아웃_성공() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound();
    }
}