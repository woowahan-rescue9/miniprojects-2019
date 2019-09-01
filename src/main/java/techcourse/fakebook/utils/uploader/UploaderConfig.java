package techcourse.fakebook.utils.uploader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:upload.properties")
public class UploaderConfig {
    @Value("${upload.article.path}")
    public String articlePath;

    @Value("${upload.user.profile.path}")
    public String userProfilePath;

    @Value("${upload.user.profile.default-path}")
    public String userProfileDefaultPath;

    @Value("${upload.user.profile.default-name}")
    public String userProfileDefaultName;
}
