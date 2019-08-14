package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.service.dto.LoginRequest;
import techcourse.fakebook.service.dto.UserSignupRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestHelper {
    private static final Logger log = LoggerFactory.getLogger(ControllerTestHelper.class);

    protected static Long newUserRequestId = 1L;

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    protected UserSignupRequest newUserSignupRequest() {
        UserSignupRequest userSignupRequest = new UserSignupRequest(String.format("email%d@hello.com", newUserRequestId++),
                "Password!1",
                "name",
                "gender",
                "coverUrl",
                "birth",
                "introduction");

        log.debug("userSignupRequest: {}", userSignupRequest);

        return userSignupRequest;
    }

    protected ResponseSpec signup(UserSignupRequest userSignupRequest) {
        return webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", userSignupRequest.getEmail())
                        .with("password", userSignupRequest.getPassword())
                        .with("name", userSignupRequest.getName())
                        .with("gender", userSignupRequest.getGender())
                        .with("coverUrl", userSignupRequest.getCoverUrl())
                        .with("birth", userSignupRequest.getBirth())
                        .with("introduction", userSignupRequest.getIntroduction())
                )
                .exchange();
    }

    protected Long getId(String email) {
        return userRepository.findByEmail(email).get().getId();
    }

    protected ResponseSpec login(LoginRequest loginRequest) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", loginRequest.getEmail())
                        .with("password", loginRequest.getPassword()))
                .exchange();
    }

    protected String getCookie(ResponseSpec rs) {
        return rs.returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}
