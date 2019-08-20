package techcourse.fakebook.service.dto;

import techcourse.fakebook.utils.NotExistsEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

//@EqualFields(baseField = "password", matchField = "reconfirmPassword")
public class UserSignupRequest {
    @NotBlank(message = "* 이메일을 작성해주세요!")
    @Email(message = "* 이메일 양식을 지켜주세요!")
    @NotExistsEmail
    private String email;

    @NotBlank(message = "* 성을 입력해주세요!")
    private String lastName;

    @NotBlank(message = "* 이름을 입력해주세요!")
    private String firstName;

    @NotBlank(message = "* 비밀번호를 작성해주세요!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}",
            message = "* 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이어야 합니다!")
    private String password;

    private String gender;
    private String birth;

    public UserSignupRequest() {
    }

    public UserSignupRequest(
            String email,
            String lastName,
            String firstName,
            String password,
            String gender,
            String birth
    ) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.gender = gender;
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "UserSignupRequest{" +
                "email='" + email + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", birth='" + birth + '\'' +
                '}';
    }
}
