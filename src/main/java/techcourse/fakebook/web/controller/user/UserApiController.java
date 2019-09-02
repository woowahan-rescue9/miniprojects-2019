package techcourse.fakebook.web.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.exception.InvalidSignupException;
import techcourse.fakebook.service.article.TotalService;
import techcourse.fakebook.service.article.dto.TotalArticleResponse;
import techcourse.fakebook.service.user.LoginService;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserApiController {
    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final UserService userService;
    private final LoginService loginService;
    private final TotalService totalService;

    public UserApiController(UserService userService, LoginService loginService, TotalService totalService) {
        this.userService = userService;
        this.loginService = loginService;
        this.totalService = totalService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserSignupRequest userSignupRequest, BindingResult bindingResult, HttpSession session) {
        log.debug("begin");

        if (bindingResult.hasErrors()) {
            throw new InvalidSignupException(bindingResult.getFieldError().getDefaultMessage());
        }

        userService.save(userSignupRequest);

        LoginRequest loginRequest = new LoginRequest(userSignupRequest.getEmail(), userSignupRequest.getPassword());
        UserOutline userOutline = loginService.login(loginRequest);
        session.setAttribute(LoginController.SESSION_USER_KEY, userOutline);

        return ResponseEntity.created(URI.create("/users/" + userOutline.getId())).build();
    }

    @GetMapping("/{userId}/info")
    public ResponseEntity<UserResponse> userInfo(@PathVariable Long userId) {
        log.debug("begin");

        UserResponse userResponse = userService.findById(userId);

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/{userId}/articles")
    public ResponseEntity<List<TotalArticleResponse>> showArticles(@PathVariable Long userId) {
        List<TotalArticleResponse> articles = totalService.findArticlesByUser(userId);
        return ResponseEntity.ok().body(articles);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(@PathVariable Long userId,
                                               @Valid UserUpdateRequest userUpdateRequest, HttpSession session) {
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
    public ResponseEntity<List<UserResponse>> search(@PathVariable String keyword) {
        log.debug("begin");
        log.debug("keyword : {}", keyword);

        List<UserResponse> userResponses = userService.findUserNamesByKeyword(keyword);

        return ResponseEntity.ok(userResponses);
    }
}
