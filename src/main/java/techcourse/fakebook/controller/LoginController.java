package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.service.LoginService;
import techcourse.fakebook.service.dto.LoginRequest;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final String SESSION_USER_KEY = "user";

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpSession session) {
        log.debug("begin");

        SessionUser sessionUser = loginService.login(loginRequest);

        session.setAttribute(SESSION_USER_KEY, sessionUser);

        return "redirect:/timeline";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.debug("begin");

        session.removeAttribute(SESSION_USER_KEY);

        return "redirect:/";
    }
}
