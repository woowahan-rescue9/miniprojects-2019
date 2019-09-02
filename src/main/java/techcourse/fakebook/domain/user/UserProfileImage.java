package techcourse.fakebook.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserProfileImage {
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
