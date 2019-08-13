package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.service.UserService;
import techcourse.fakebook.service.dto.UserSignupRequest;
import techcourse.fakebook.service.dto.UserResponse;

@Controller
@RequestMapping("/users")
public class UserWebController {
    private static final Logger log = LoggerFactory.getLogger(UserWebController.class);

    private final UserService userService;

    public UserWebController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String create(UserSignupRequest userSignupRequest) {
        log.debug("begin");

        userService.save(userSignupRequest);

        return "redirect:/timeline";
    }

    @GetMapping("/{userId}")
    public String show(@PathVariable Long userId, Model model){
        log.debug("begin");

        UserResponse userResponse = userService.findById(userId);
        model.addAttribute("user", userResponse);
        return "mypage";
    }

    @DeleteMapping("/{userId}")
    public String delete(@PathVariable Long userId) {
        log.debug("begin");

        userService.deleteById(userId);

        return "redirect:/";
    }
}
