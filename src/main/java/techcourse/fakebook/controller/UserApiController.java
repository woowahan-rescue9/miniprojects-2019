package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.service.UserService;
import techcourse.fakebook.service.dto.UserResponse;
import techcourse.fakebook.service.dto.UserUpdateRequest;

@RestController
@RequestMapping("/api/users")
public class UserApiController {
    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(@PathVariable Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        log.debug("begin");
        log.debug("userUpdateRequest : {}", userUpdateRequest);

        UserResponse userResponse = userService.update(userId, userUpdateRequest);
        log.debug("userResponse : {}", userResponse);
        return ResponseEntity.ok(userResponse);
    }
}
