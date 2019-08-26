package techcourse.fakebook.web.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.service.user.dto.UserResponse;
import techcourse.fakebook.service.user.dto.UserUpdateRequest;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserApiController {
    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/info")
    public ResponseEntity<UserResponse> userInfo(@PathVariable Long userId) {
        log.debug("begin");

        UserResponse userResponse = userService.findById(userId);

        return ResponseEntity.ok().body(userResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(@PathVariable Long userId,
                                               UserUpdateRequest userUpdateRequest, HttpSession session) {
        log.debug("begin");
        log.debug("userUpdateRequest : {}", userUpdateRequest);

        UserResponse userResponse = userService.update(userId, userUpdateRequest);
        log.debug("userResponse : {}", userResponse);

        // TODO: 테스트 필요 (세션의 변경을 확인 할 방법)
        UserOutline userOutline = userService.getUserOutline(userId);
        session.setAttribute(LoginController.SESSION_USER_KEY, userOutline);

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<List<UserResponse>> update(@PathVariable String keyword) {
        log.debug("begin");
        log.debug("keyword : {}", keyword);

        List<UserResponse> userResponses = userService.findUserNamesByKeyword(keyword);

        return ResponseEntity.ok(userResponses);
    }
}
