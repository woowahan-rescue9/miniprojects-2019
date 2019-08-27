package techcourse.fakebook.domain.user;

import techcourse.fakebook.domain.BaseEntity;
import techcourse.fakebook.utils.validator.FullName;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;

@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @FullName
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Embedded
    private UserProfileImage profileImage;

    @Column(nullable = false)
    private String birth;

    private String introduction;


    private User() {
    }

    public User(String email, String encryptedPassword, String name, String gender, UserProfileImage profileImage, String birth, String introduction) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.name = name;
        this.gender = gender;
        this.profileImage = profileImage;
        this.birth = birth;
        this.introduction = introduction;
    }

    public void updateModifiableFields(String name, String encryptedPassword, String introduction, UserProfileImage profileImage) {
        this.name = name;
        this.encryptedPassword = encryptedPassword;
        this.introduction = introduction;
        this.profileImage = profileImage;
    }

    public void updateModifiableFields(UserProfileImage coverUrl, String introduction) {
        this.profileImage = coverUrl;
        this.introduction = introduction;
    }

    public boolean isSameWith(Long id) {
        return this.id.equals(id);
    }

    public boolean checkEncryptedPassword(String encryptedPassword) {
        return this.encryptedPassword.equals(encryptedPassword);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public UserProfileImage getProfileImage() {
        return profileImage;
    }

    public String getBirth() {
        return birth;
    }

    public String getIntroduction() {
        return introduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", birth='" + birth + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
