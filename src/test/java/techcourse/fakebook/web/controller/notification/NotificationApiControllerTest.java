package techcourse.fakebook.web.controller.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.web.controller.ControllerTestHelper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class NotificationApiControllerTest extends ControllerTestHelper {
    @LocalServerPort
    private int port;
    private final LoginRequest loginRequest = new LoginRequest("van@van.com", "Password!1");
    private String sessionId;

    @BeforeEach
    void setUp() {
        sessionId = getSessionId(login(loginRequest));
    }

    @Test
    void 새로운_채널_주소_발급() {
        given().
                port(port).
                sessionId(sessionId).
        when().
                get("/api/notification").
        then().
                statusCode(HttpStatus.OK.value()).
                body("address.length()", equalTo(32));
    }
}