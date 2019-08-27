package techcourse.fakebook.service.user.dto;

import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.utils.validator.FullName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserUpdateRequest {
    private MultipartFile profileImage;
    private String introduction;
    @FullName
    private String name;
    @NotBlank(message = "* 비밀번호를 작성해주세요!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()])[A-Za-z\\d~!@#$%^&*()]{8,}",
            message = "* 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이어야 합니다!")
    private String password;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(MultipartFile profileImage, String introduction, String name, String password) {
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.name = name;
        this.password = password;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
