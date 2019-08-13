package techcourse.fakebook.utils;

import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.dto.UserDto;

public class UserAssembler {
    private UserAssembler() {
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName());
    }
}
