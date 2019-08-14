package techcourse.fakebook.domain.user;

import techcourse.fakebook.domain.DateTime;

import javax.persistence.*;

@Entity
public class User extends DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String introduction;

    private User() {
    }

    public User(String email, String encryptedPassword, String name, String gender, String coverUrl, String birth, String introduction) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.name = name;
        this.gender = gender;
        this.coverUrl = coverUrl;
        this.birth = birth;
        this.introduction = introduction;
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getBirth() {
        return birth;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void updateModifiableFields(String coverUrl, String introduction) {
        this.coverUrl = coverUrl;
        this.introduction = introduction;
    }

    public boolean isNotAuthor(Long id) {
        return !this.id.equals(id);
    }

    public boolean checkEncryptedPassword(String encryptedPassword) {
        return this.encryptedPassword.equals(encryptedPassword);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", birth='" + birth + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
