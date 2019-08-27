package techcourse.fakebook.service.user.dto;

import com.sun.xml.internal.ws.api.message.Attachment;
import techcourse.fakebook.domain.user.UserAttachment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserUpdateRequest {
    private String profileImage;
    private String introduction;
    private String name;
    @NotBlank(message = "* 비밀번호를 작성해주세요!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()])[A-Za-z\\d~!@#$%^&*()]{8,}",
            message = "* 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이어야 합니다!")
    private String password;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String profileImage, String introduction, String name, String password) {
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.name = name;
        this.password = password;
    }

    public String getProfileImage() {
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

    public void setProfileImage(String profileImage) {
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
