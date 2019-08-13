package techcourse.fakebook.utils;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.User;
import techcourse.fakebook.service.dto.UserRequest;
import techcourse.fakebook.service.dto.UserResponse;
import techcourse.fakebook.utils.encryptor.Encryptor;

@Component
public class UserAssembler {
    private Encryptor encryptor;

    public UserAssembler(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public User toEntity(UserRequest userRequest) {
        return new User(
                userRequest.getEmail(),
                encryptor.encrypt(userRequest.getPassword()),
                userRequest.getName(),
                userRequest.getGender(),
                userRequest.getCoverUrl(),
                userRequest.getBirth(),
                userRequest.getIntroduction()
        );
    }

    public UserResponse toResponse(User savedUser) {
        return null;
    }
}
