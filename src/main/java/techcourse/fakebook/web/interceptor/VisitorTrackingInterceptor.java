package techcourse.fakebook.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.fakebook.service.friendship.VisitorTrackingService;
import techcourse.fakebook.service.user.dto.UserOutline;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Component
public class VisitorTrackingInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(NotLoginedInterceptor.class);

    private final VisitorTrackingService visitorTrackingService;

    public VisitorTrackingInterceptor(VisitorTrackingService visitorTrackingService) {
        this.visitorTrackingService = visitorTrackingService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("begin");

        Optional<UserOutline> sessionUser = Optional.ofNullable((UserOutline) request.getSession().getAttribute("user"));

        sessionUser.ifPresent(userOutline -> {
            Long visitorId = userOutline.getId();

            // 상수를 잘 모아놓았으면.. 어디에 모으는 것이 좋을까?
            final Map<String, String> pathVariables = (Map<String, String>) request
                    .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            Long userId = Long.parseLong(pathVariables.get("userId"));

            log.debug("userId: {}, visitorId: {}", userId, visitorId);

            visitorTrackingService.visit(userId, visitorId);
        });
        return true;
    }
}
