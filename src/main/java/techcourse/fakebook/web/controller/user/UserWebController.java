package techcourse.fakebook.web.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.fakebook.service.article.TotalService;
import techcourse.fakebook.service.friendship.FriendshipService;
import techcourse.fakebook.service.user.LoginService;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.service.user.dto.UserResponse;
import techcourse.fakebook.web.argumentresolver.OptionalSessionUser;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserWebController {
    private static final Logger log = LoggerFactory.getLogger(UserWebController.class);

    private final UserService userService;
    private final LoginService loginService;
    private final TotalService totalService;
    private final FriendshipService friendshipService;

    public UserWebController(UserService userService, LoginService loginService, TotalService totalService, FriendshipService friendshipService) {
        this.userService = userService;
        this.loginService = loginService;
        this.totalService = totalService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/{userId}")
    public String show(@PathVariable Long userId, @OptionalSessionUser Optional<UserOutline> userOutline, Model model) {
        log.debug("begin");

        UserResponse userResponse = userService.findById(userId);
        model.addAttribute("user", userResponse);
        model.addAttribute("articles", totalService.findArticlesByUser(userId));

        userOutline.ifPresent(loggedInUserOutline -> {
            log.debug("logged-in user..!!");
            model.addAttribute("isFriend", friendshipService.hasFriendship(loggedInUserOutline.getId(), userId));
        });

        return "profile";
    }

    @DeleteMapping("/{userId}")
    public String delete(@PathVariable Long userId) {
        log.debug("begin");

        userService.deleteById(userId);

        return "redirect:/";
    }
}
