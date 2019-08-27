package techcourse.fakebook.web.controller.user;

import org.junit.jupiter.api.Test;

import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.web.controller.ControllerTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

class UserWebControllerTest extends ControllerTestHelper {
    @Test
    void 존재하는_유저조회() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        signup(userSignupRequest);
        Long userId = getId(userSignupRequest.getEmail());

        webTestClient.get().uri("/users/" + userId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("성이름")).isTrue();
                });
    }

    @Test
    void 로그인된_존재하는_유저삭제() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String cookie = getSessionId(signup(userSignupRequest));
        Long userId = getId(userSignupRequest.getEmail());

        login(new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword()));

        webTestClient.delete().uri("/users/" + userId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound().expectHeader().valueMatches("location", ".*/");

        // TODO: 삭제여부
        // 애러페이지
    }

    @Test
    void 비로그인_존재하는_유저삭제() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        signup(userSignupRequest);
        Long userId = getId(userSignupRequest.getEmail());

        webTestClient.delete().uri("/users/" + userId)
                .exchange()
                .expectStatus()
                .isFound().expectHeader().valueMatches("location", ".*/");
    }
}