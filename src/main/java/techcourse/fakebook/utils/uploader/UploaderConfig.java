package techcourse.fakebook.utils.uploader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:upload.properties")
@ConfigurationProperties(prefix = "upload")
public class UploaderConfig {
    private String articlePath;
    private String userProfilePath;
    private String userProfileDefaultPath;
    private String userProfileDefaultName;

    public String getArticlePath() {
        return articlePath;
    }

    public void setArticlePath(String articlePath) {
        this.articlePath = articlePath;
    }

    public String getUserProfilePath() {
        return userProfilePath;
    }

    public void setUserProfilePath(String userProfilePath) {
        this.userProfilePath = userProfilePath;
    }

    public String getUserProfileDefaultPath() {
        return userProfileDefaultPath;
    }

    public void setUserProfileDefaultPath(String userProfileDefaultPath) {
        this.userProfileDefaultPath = userProfileDefaultPath;
    }

    public String getUserProfileDefaultName() {
        return userProfileDefaultName;
    }

    public void setUserProfileDefaultName(String userProfileDefaultName) {
        this.userProfileDefaultName = userProfileDefaultName;
    }
}
