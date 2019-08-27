package techcourse.fakebook.service.user.dto;

import techcourse.fakebook.service.article.dto.AttachmentResponse;

public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String gender;
    private AttachmentResponse profileImage;
    private String birth;
    private String introduction;

    public UserResponse(Long id, String email, String name, String gender, AttachmentResponse profileImage, String birth, String introduction) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.profileImage = profileImage;
        this.birth = birth;
        this.introduction = introduction;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public AttachmentResponse getProfileImage() {
        return profileImage;
    }

    public String getBirth() {
        return birth;
    }

    public String getIntroduction() {
        return introduction;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", birth='" + birth + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
