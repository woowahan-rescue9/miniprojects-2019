package techcourse.fakebook.utils;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.User;
import techcourse.fakebook.service.dto.UserSignupRequest;
import techcourse.fakebook.service.dto.UserResponse;
import techcourse.fakebook.utils.encryptor.Encryptor;

@Component
public class UserAssembler {
    private Encryptor encryptor;

    public UserAssembler(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public User toEntity(UserSignupRequest userSignupRequest) {
        return new User(
                userSignupRequest.getEmail(),
                encryptor.encrypt(userSignupRequest.getPassword()),
                userSignupRequest.getName(),
                userSignupRequest.getGender(),
                userSignupRequest.getCoverUrl(),
                userSignupRequest.getBirth(),
                userSignupRequest.getIntroduction()
        );
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getGender(),
                user.getCoverUrl(),
                user.getBirth(),
                user.getIntroduction()
        );
    }
}
