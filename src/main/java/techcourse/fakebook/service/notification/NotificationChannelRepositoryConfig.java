package techcourse.fakebook.service.notification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationChannelRepositoryConfig {
    @Bean
    public NotificationChannelRepository notificationChannelRepository() {
        return new NotificationChannelRepository();
    }
}