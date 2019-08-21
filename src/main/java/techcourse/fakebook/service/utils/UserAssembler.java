package techcourse.fakebook.service.utils;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.service.dto.UserResponse;
import techcourse.fakebook.service.dto.UserSignupRequest;
import techcourse.fakebook.service.utils.encryptor.Encryptor;

@Component
public class UserAssembler {
    private static final String DEFAULT_PROFILE_PHOTO = "default.png";
    private final Encryptor encryptor;

    public UserAssembler(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public static UserOutline toUserOutline(User user) {
        return new UserOutline(user.getId(),
                user.getName(),
                user.getCoverUrl());
    }

    public User toEntity(UserSignupRequest userSignupRequest) {
        return new User(
                userSignupRequest.getEmail(),
                encryptor.encrypt(userSignupRequest.getPassword()),
                userSignupRequest.getLastName() + userSignupRequest.getFirstName(),
                userSignupRequest.getGender(),
                DEFAULT_PROFILE_PHOTO,
                userSignupRequest.getBirth(),
                ""
        );
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getGender(),
                user.getCoverUrl(),
                user.getBirth(),
                user.getIntroduction()
        );
    }
}
