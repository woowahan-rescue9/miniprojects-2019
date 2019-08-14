package techcourse.fakebook.controller.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.fakebook.service.dto.UserOutline;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class AlreadyLoginedInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AlreadyLoginedInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("begin");

        Optional<UserOutline> sessionUser = Optional.ofNullable((UserOutline) request.getSession().getAttribute("user"));

        if (request.getRequestURI().matches(".*/users.*") && !request.getMethod().equals("POST")) {
            return true;
        }

        if (sessionUser.isPresent()) {
            response.sendRedirect("/timeline");
            return false;
        }
        return true;
    }
}
