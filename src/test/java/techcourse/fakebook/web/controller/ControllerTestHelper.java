package techcourse.fakebook.web.controller;

import io.restassured.response.ValidatableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.service.article.dto.ArticleResponse;
import techcourse.fakebook.service.user.dto.*;
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

    protected ValidatableResponse signup(UserSignupRequest userSignupRequest) {
        return given().
                        port(port).
                        contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                        body(userSignupRequest).
                when().
                        post("/api/users").
                then();
    }

    protected ArticleResponse writeArticle() {
        LoginRequest loginRequest = new LoginRequest("van@van.com", "Password!1");
        String sessionId = getSessionId(login(loginRequest));

        return given().
                port(port).
                sessionId(sessionId).
                formParam("content", "hello").
        when().
                post("/api/articles").
                as(ArticleResponse.class);
    }

    protected Long getId(String email) {
        return userRepository.findByEmail(email).get().getId();
    }

    protected ValidatableResponse login(LoginRequest loginRequest) {
        return given().
                    port(port).
                    contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                    body(loginRequest).
                when().
                    post("/api/login").
                then();
    }

    protected String getSessionId(ValidatableResponse vs) {
        return vs.extract()
                .response()
                .getSessionId();
    }
}