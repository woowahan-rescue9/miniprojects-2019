package techcourse.fakebook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.fakebook.service.UserService;
import techcourse.fakebook.service.dto.UserRequest;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String create(UserRequest userRequest) {

        userService.save(userRequest);


        return "redirect:/timeline";
    }
}
