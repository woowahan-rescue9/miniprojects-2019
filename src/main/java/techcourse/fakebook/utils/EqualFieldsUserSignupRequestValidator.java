package techcourse.fakebook.utils;

import techcourse.fakebook.service.dto.UserSignupRequest;

public class EqualFieldsUserSignupRequestValidator extends EqualFieldsValidator<UserSignupRequest> {
    @Override
    public String getMessage() {
        return "비밀번호가 일치하지 않습니다";
    }
}
