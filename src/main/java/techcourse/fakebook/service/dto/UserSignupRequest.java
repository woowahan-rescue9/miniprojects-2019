package techcourse.fakebook.service.dto;

public class UserSignupRequest {
    private String email;
    private String password;
    private String name;
    private String gender;
    private String coverUrl;
    private String birth;
    private String introduction;

    public UserSignupRequest(String email, String password, String name, String gender, String coverUrl, String birth, String introduction) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.coverUrl = coverUrl;
        this.birth = birth;
        this.introduction = introduction;

        if (this.coverUrl == null) {
            this.coverUrl = "";
        }
        if (this.introduction == null) {
            this.introduction = "";
        }
        //가입 시가 아닌 프로필 수정 때에 설정하는 부분인데 nullable = false로 되어 있어서 가입이 불가능하여 임시로 처리하였으므로 적절한 정리 요청
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getBirth() {
        return birth;
    }

    public String getIntroduction() {
        return introduction;
    }

    @Override
    public String toString() {
        return "UserSignupRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", birth='" + birth + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
