package techcourse.fakebook.utils.validator;

import techcourse.fakebook.service.user.dto.UserSignupRequest;

public class EqualFieldsUserSignupRequestValidator extends EqualFieldsValidator<UserSignupRequest> {
    @Override
    public String getMessage() {
        return "비밀번호가 일치하지 않습니다";
    }
}
