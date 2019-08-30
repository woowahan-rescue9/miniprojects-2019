package techcourse.fakebook.web.controller.friendship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.friendship.dto.FriendshipRequest;
import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.web.controller.ControllerTestHelper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class FriendshipApiControllerTest extends ControllerTestHelper {
    private static final Logger log = LoggerFactory.getLogger(FriendshipApiControllerTest.class);

    @LocalServerPort
    private int port;

    private int numUsers = 6;
    private List<UserSignupRequest> userSignupRequests;
    private List<Long> userIds;

    private Long loginedUserId;
    private String sessionId;

    @BeforeEach
    void Setup() {
        userSignupRequests = IntStream.range(0, numUsers)
                .mapToObj(num -> newUserSignupRequest())
                .collect(Collectors.toList());
        userSignupRequests.stream()
                .forEach(request -> signup(request));
        userIds = userSignupRequests.stream()
                .map(request -> getId(request.getEmail()))
                .collect(Collectors.toList());

        int loginedUserIndex = 0;
        loginedUserId = userIds.get(loginedUserIndex);
        UserSignupRequest loginedUserSignupRequest = userSignupRequests.get(loginedUserIndex);
        sessionId = getSessionId(login(new LoginRequest(loginedUserSignupRequest.getEmail(), loginedUserSignupRequest.getPassword())));

        log.debug("sessionId: {}", sessionId);
    }

    @Test
    void 친구_전부_불러오기() {
        int friendIndex = 3;
        Long friendId = userIds.get(friendIndex);

        // 친구 요청
        친구_요청(friendIndex);

        // 친구 불러오기
        given().
                port(port).
                sessionId(sessionId).
        when().
                get("/api/friendships/" + loginedUserId).
        then().
                statusCode(HttpStatus.OK.value()).
                body("size", greaterThanOrEqualTo(1)).
                body("id", hasItem((int) (long) friendId));
    }

    @Test
    void 로그인_안_된_상태에서_친구_요청() {
        int friendIndex = 5;
        Long friendId = userIds.get(friendIndex);
        FriendshipRequest friendshipRequest = new FriendshipRequest(friendId);

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(friendshipRequest).
        when().
                post("/api/friendships").
        then().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 존재하지_않는_friendId로_친구_요청() {
        Long notExistsFriendId = -1L;
        FriendshipRequest friendshipRequest = new FriendshipRequest(notExistsFriendId);

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(sessionId).
                body(friendshipRequest).
        when().
                post("/api/friendships").
        then().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 친구_요청_성공() {
        int friendIndex = 5;
        Long friendId = userIds.get(friendIndex);
        FriendshipRequest friendshipRequest = new FriendshipRequest(friendId);

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(sessionId).
                body(friendshipRequest).
        when().
                post("/api/friendships").
        then().
                statusCode(HttpStatus.OK.value());
    }

    @Test
    void 로그인_안_된_상태에서_친구_제거() {
        int friendIndex = 5;
        Long friendId = userIds.get(friendIndex);

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
        when().
                delete("/api/friendships" + "?friendId=" + friendId).
        then().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 존재하지_않는_friendId로_친구_제거() {
        Long notExistsFriendId = -1L;

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(sessionId).
        when().
                delete("/api/friendships" + "?friendId=" + notExistsFriendId).
        then().
                statusCode(HttpStatus.OK.value());
    }

    @Test
    void 친구_요청_후_친구_제거() {
        int friendIndex = 5;
        친구_요청(friendIndex);

        Long friendId = userIds.get(friendIndex);

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(sessionId).
        when().
                delete("/api/friendships" + "?friendId=" + friendId).
        then().
                statusCode(HttpStatus.OK.value());

        // 삭제 되었어야만 친구_요청 이 성공
        친구_요청(friendIndex);
    }

    private void 친구_요청(int friendIndex) {
        Long friendId = userIds.get(friendIndex);
        FriendshipRequest friendshipRequest = new FriendshipRequest(friendId);

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                sessionId(sessionId).
                body(friendshipRequest).
        when().
                post("/api/friendships").
        then().
                statusCode(HttpStatus.OK.value());
    }
}