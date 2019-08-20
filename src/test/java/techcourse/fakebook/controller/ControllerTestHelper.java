package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.LoginRequest;
import techcourse.fakebook.service.dto.UserSignupRequest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestHelper {
    private static final Logger log = LoggerFactory.getLogger(ControllerTestHelper.class);

    protected static Long newUserRequestId = 1L;

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    @LocalServerPort
    private int port;

    protected UserSignupRequest newUserSignupRequest() {
        UserSignupRequest userSignupRequest = new UserSignupRequest(String.format("email%d@hello.com", newUserRequestId++),
                "성",
                "이름",
                "Password!1",
                "gender",
                "birth");

        log.debug("userSignupRequest: {}", userSignupRequest);

        return userSignupRequest;
    }

    protected ResponseSpec signup(UserSignupRequest userSignupRequest) {
        return webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", userSignupRequest.getEmail())
                        .with("password", userSignupRequest.getPassword())
                        .with("lastName", userSignupRequest.getLastName())
                        .with("firstName", userSignupRequest.getFirstName())
                        .with("gender", userSignupRequest.getGender())
                        .with("birth", userSignupRequest.getBirth())
                )
                .exchange();
    }

    protected ArticleResponse writeArticle() {
        LoginRequest loginRequest = new LoginRequest("van@van.com", "Password!1");
        String cookie = getCookie(login(loginRequest));
        ArticleRequest articleRequest = new ArticleRequest("hello");

        return given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
        body(articleRequest).
                when().
                post("/api/articles").
                as(ArticleResponse.class);
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
