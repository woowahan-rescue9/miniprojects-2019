package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.fakebook.service.dto.UserRequest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 올바른입력_유저생성() {
        UserRequest userRequest = new UserRequest("email",
                "Password!1",
                "name",
                "gender",
                "coverUrl",
                "birth",
                "introduction");

        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", userRequest.getEmail())
                        .with("password", userRequest.getPassword())
                        .with("name", userRequest.getName())
                        .with("gender", userRequest.getGender())
                        .with("coverUrl", userRequest.getCoverUrl())
                        .with("birth", userRequest.getBirth())
                        .with("introduction", userRequest.getIntroduction())
                )
                .exchange()
                .expectStatus()
                .isFound();

        // get ? 실제로 들어있는지 확인
    }
}