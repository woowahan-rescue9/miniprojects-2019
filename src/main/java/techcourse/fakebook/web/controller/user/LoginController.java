package techcourse.fakebook.web.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.fakebook.service.user.LoginService;
import techcourse.fakebook.service.user.dto.LoginRequest;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    public static final String SESSION_USER_KEY = "user";

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String index(UserSignupRequest userSignupRequest) {
        log.debug("begin");

        return "index";
    }

    @ResponseBody
    @PostMapping("/api/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        log.debug("begin");

        UserOutline userOutline = loginService.login(loginRequest);

        session.setAttribute(SESSION_USER_KEY, userOutline);
        log.debug("userOutline: {}", userOutline);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.debug("begin");

        session.removeAttribute(SESSION_USER_KEY);

        return "redirect:/";
    }
}
