package techcourse.fakebook.service.user.dto;

import techcourse.fakebook.service.article.dto.AttachmentResponse;

import java.util.Objects;

public class UserOutline {
    private Long id;
    private String name;
    private AttachmentResponse profileImage;

    private UserOutline() {
    }

    public UserOutline(Long id, String name, AttachmentResponse profileImage) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AttachmentResponse getProfileImage() {
        return profileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOutline that = (UserOutline) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(profileImage, that.profileImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profileImage);
    }

    @Override
    public String toString() {
        return "UserOutline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
