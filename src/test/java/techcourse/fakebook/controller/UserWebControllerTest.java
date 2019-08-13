package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import techcourse.fakebook.service.dto.UserSignupRequest;

import static org.assertj.core.api.Assertions.assertThat;

class UserWebControllerTest extends ControllerTestHelper {
    @Test
    void 유저생성_올바른_입력() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        signup(userSignupRequest).expectStatus()
                .isFound();
    }

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
                    assertThat(body.contains(userSignupRequest.getEmail())).isTrue();
                    assertThat(body.contains(userSignupRequest.getName())).isTrue();
                    assertThat(body.contains(userSignupRequest.getGender())).isTrue();
                    assertThat(body.contains(userSignupRequest.getCoverUrl())).isTrue();
                    assertThat(body.contains(userSignupRequest.getBirth())).isTrue();
                    assertThat(body.contains(userSignupRequest.getIntroduction())).isTrue();
                });
    }

    @Test
    void 존재하는_유저삭제() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        signup(userSignupRequest);
        Long userId = getId(userSignupRequest.getEmail());

        webTestClient.delete().uri("/users/" + userId)
                .exchange()
                .expectStatus()
                .isFound();

        // TODO: 삭제여부
        // 애러페이지
    }
}