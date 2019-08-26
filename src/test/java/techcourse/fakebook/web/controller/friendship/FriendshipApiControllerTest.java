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

class FriendshipApiControllerTest extends ControllerTestHelper {
    private static final Logger log = LoggerFactory.getLogger(FriendshipApiControllerTest.class);

    @LocalServerPort
    private int port;

    private int numUsers = 6;
    private List<UserSignupRequest> userSignupRequests;
    private List<Long> userIds;


    private Long loginedUserId;
    private String cookie;

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
        cookie = getCookie(login(new LoginRequest(loginedUserSignupRequest.getEmail(), loginedUserSignupRequest.getPassword())));

        log.debug("cookie: {}", cookie);
    }


    @Test
    void 로그인_안_된_상태에서_친구요청() {
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
    void 존재하지_않는_friendId로_친구요청() {
        Long notExistsFriendId = -1L;
        FriendshipRequest friendshipRequest = new FriendshipRequest(notExistsFriendId);

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
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
                cookie(cookie).
                body(friendshipRequest).
        when().
                post("/api/friendships").
        then().
                statusCode(HttpStatus.OK.value());
    }

    @Test
    void 로그인_안_된_상태에서_친구제거() {
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
    void 존재하지_않는_friendId로_친구제거() {
        Long notExistsFriendId = -1L;

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
        when().
                delete("/api/friendships" + "?friendId=" + notExistsFriendId).
        then().
                statusCode(HttpStatus.OK.value());
    }

    @Test
    void 친구요청후_친구제거() {
        int friendIndex = 5;
        친구_요청(friendIndex);

        Long friendId = userIds.get(friendIndex);

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                cookie(cookie).
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
                cookie(cookie).
                body(friendshipRequest).
        when().
                post("/api/friendships").
        then().
                statusCode(HttpStatus.OK.value());;
    }
}