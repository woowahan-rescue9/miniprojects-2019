package techcourse.fakebook.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserProfileImage {
    public static String USER_STATIC_FILE_PATH = "user/profile/";
    public static String DEFAULT_IMAGE_NAME = "default.png";
    public static String DEFAULT_IMAGE_PATH = "https://woowa-rescue9.s3.ap-northeast-2.amazonaws.com/user/profile/default.png";

    @Column(nullable = false, name = "profile_image_name")
    private String name;

    @Column(nullable = false, name = "profile_image_path")
    private String path;

    private UserProfileImage() {
    }

    public UserProfileImage(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
