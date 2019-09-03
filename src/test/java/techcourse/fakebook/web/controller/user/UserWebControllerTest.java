package techcourse.fakebook.web.controller.user;

import org.junit.jupiter.api.Test;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.web.controller.ControllerTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

class UserWebControllerTest extends ControllerTestHelper {
    private static final String JSESSIONID = "JSESSIONID";

    @Test
    void 존재하는_유저조회() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        String jsessionid = getSessionId(signup(userSignupRequest));
        Long userId = getId(userSignupRequest.getEmail());

        webTestClient.get().uri("/users/" + userId)
                .cookie(JSESSIONID, jsessionid)
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
        String jsessionid = getSessionId(signup(userSignupRequest));
        Long userId = getId(userSignupRequest.getEmail());

        webTestClient.delete().uri("/users/" + userId)
                .cookie(JSESSIONID, jsessionid)
                .exchange()
                .expectStatus()
                .isFound().expectHeader().valueMatches("location", ".*/");
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