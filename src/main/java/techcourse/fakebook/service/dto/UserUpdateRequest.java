package techcourse.fakebook.service.dto;

public class UserUpdateRequest {
    private String coverUrl;
    private String introduction;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String coverUrl, String introduction) {
        this.coverUrl = coverUrl;
        this.introduction = introduction;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "UserUpdateRequest{" +
                "coverUrl='" + coverUrl + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
