package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.fakebook.domain.UserRepository;
import techcourse.fakebook.service.dto.UserRequest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserWebControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private Long newUserRequestId = 1l;

    @Test
    void 유저생성_올바른_입력() {
        UserRequest userRequest = newUserRequest();
        signup(webTestClient, userRequest).expectStatus()
                .isFound();
    }

    @Test
    void 존재하는_유저조회() {
        UserRequest userRequest = newUserRequest();
        signup(webTestClient, userRequest);
        Long userId = getId(userRequest.getEmail());
        webTestClient.get().uri("/users/" + userId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(userRequest.getEmail())).isTrue();
                    assertThat(body.contains(userRequest.getName())).isTrue();
                    assertThat(body.contains(userRequest.getGender())).isTrue();
                    assertThat(body.contains(userRequest.getCoverUrl())).isTrue();
                    assertThat(body.contains(userRequest.getBirth())).isTrue();
                    assertThat(body.contains(userRequest.getIntroduction())).isTrue();
                });
    }

    @Test
    void 유저수정_올바른_입력() {

    }

    private UserRequest newUserRequest() {
        return new UserRequest(String.format("email%d@hello.com", newUserRequestId++),
                "Password!1",
                "name",
                "gender",
                "coverUrl",
                "birth",
                "introduction");
    }

    private ResponseSpec signup(WebTestClient webTestClient, UserRequest userRequest) {
        return webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", userRequest.getEmail())
                        .with("password", userRequest.getPassword())
                        .with("name", userRequest.getName())
                        .with("gender", userRequest.getGender())
                        .with("coverUrl", userRequest.getCoverUrl())
                        .with("birth", userRequest.getBirth())
                        .with("introduction", userRequest.getIntroduction())
                )
                .exchange();
    }

    private Long getId(String email) {
        return userRepository.findByEmail(email).get().getId();
    }
}