package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.service.UserService;
import techcourse.fakebook.service.dto.UserResponse;
import techcourse.fakebook.service.dto.UserSignupRequest;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserWebController {
    private static final Logger log = LoggerFactory.getLogger(UserWebController.class);

    private final UserService userService;

    public UserWebController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String create(@Valid UserSignupRequest userSignupRequest, BindingResult bindingResult) {
        log.debug("begin");

        if (bindingResult.hasErrors()) {
            return "index";
        }

        userService.save(userSignupRequest);

        return "redirect:/";
    }

    @GetMapping("/{userId}")
    public String show(@PathVariable Long userId, Model model){
        log.debug("begin");

        UserResponse userResponse = userService.findById(userId);
        model.addAttribute("user", userResponse);
        return "profile";
    }

    @DeleteMapping("/{userId}")
    public String delete(@PathVariable Long userId) {
        log.debug("begin");

        userService.deleteById(userId);

        return "redirect:/";
    }
}
