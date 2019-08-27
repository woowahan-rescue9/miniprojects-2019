package techcourse.fakebook.service.user.assembler;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.service.article.assembler.AttachmentAssembler;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.service.user.dto.UserResponse;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.service.user.encryptor.Encryptor;

@Component
public class UserAssembler {
    private static final String DEFAULT_PROFILE_PHOTO = "default.png";
    private final AttachmentAssembler attachmentAssembler;
    private final Encryptor encryptor;

    public UserAssembler(AttachmentAssembler attachmentAssembler, Encryptor encryptor) {
        this.attachmentAssembler = attachmentAssembler;
        this.encryptor = encryptor;
    }

    public UserOutline toUserOutline(User user) {
        return new UserOutline(user.getId(),
                user.getName(),
                attachmentAssembler.toResponse(user.getProfileImage()));
    }

    public User toEntity(UserSignupRequest userSignupRequest, UserProfileImage profileImage) {
        return new User(
                userSignupRequest.getEmail(),
                encryptor.encrypt(userSignupRequest.getPassword()),
                userSignupRequest.getLastName() + userSignupRequest.getFirstName(),
                userSignupRequest.getGender(),
                profileImage,
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
                attachmentAssembler.toResponse(user.getProfileImage()),
                user.getBirth(),
                user.getIntroduction()
        );
    }
}
