package techcourse.fakebook.utils;

import techcourse.fakebook.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NotExistsEmailValidator implements ConstraintValidator<NotExistsEmail, String> {
    private final UserService userService;

    public NotExistsEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(NotExistsEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (Objects.isNull(email)) {
            return false;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("이미 존재하는 이메일입니다")
                .addConstraintViolation();

        return userService.hasNotUserWithEmail(email);
    }
}
