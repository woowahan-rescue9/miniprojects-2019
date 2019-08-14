package techcourse.fakebook.controller.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AlreadyLoginedInterceptor alreadyLoginedInterceptor;
    private final NotLoginedInterceptor notLoginedInterceptor;

    public WebMvcConfig(AlreadyLoginedInterceptor alreadyLoginedInterceptor, NotLoginedInterceptor notLoginedInterceptor) {
        this.alreadyLoginedInterceptor = alreadyLoginedInterceptor;
        this.notLoginedInterceptor = notLoginedInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alreadyLoginedInterceptor)
                .addPathPatterns("/")
                .addPathPatterns("/users/**")
                .addPathPatterns("/api/users/**")
                .addPathPatterns("/login");

        registry.addInterceptor(notLoginedInterceptor)
                .addPathPatterns("/users/**")
                .addPathPatterns("/api/users/**")
                .addPathPatterns("/logout");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add();
    }
}