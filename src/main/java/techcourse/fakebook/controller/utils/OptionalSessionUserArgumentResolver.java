package techcourse.fakebook.controller.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.fakebook.controller.LoginController;
import techcourse.fakebook.service.dto.UserOutline;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class OptionalSessionUserArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger log = LoggerFactory.getLogger(OptionalSessionUserArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalSessionUser.class);

        // TODO:
        // 파라미터가 Optional<UserOutline> 인지 검증
    }

    @Override
    public Optional<UserOutline> resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                 NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.debug("begin");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();

        return Optional.ofNullable(session)
                .map(s -> (UserOutline) s.getAttribute(LoginController.SESSION_USER_KEY));
    }
}

