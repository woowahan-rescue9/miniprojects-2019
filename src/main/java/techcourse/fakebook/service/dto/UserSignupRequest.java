package techcourse.fakebook.service.dto;

public class UserSignupRequest {
    private String email;
    private String password;
    private String name;
    private String gender;
    private String birth;

    public UserSignupRequest(String email, String password, String name, String gender, String birth) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBirth() {
        return birth;
    }

    @Override
    public String toString() {
        return "UserSignupRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birth='" + birth + '\'' +
                '}';
    }
}
