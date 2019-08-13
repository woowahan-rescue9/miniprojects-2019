package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.fakebook.service.UserService;
import techcourse.fakebook.service.dto.UserRequest;
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
    public String create(UserRequest userRequest) {
        log.debug("begin");

        userService.save(userRequest);

        return "redirect:/timeline";
    }

    @GetMapping("/{userId}")
    public String show(@PathVariable Long userId, Model model){
        log.debug("begin");

        UserResponse userResponse = userService.findById(userId);
        model.addAttribute("user", userResponse);
        return "mypage";
    }
}
