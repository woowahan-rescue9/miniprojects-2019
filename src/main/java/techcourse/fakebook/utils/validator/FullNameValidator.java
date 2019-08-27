package techcourse.fakebook.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class FullNameValidator implements ConstraintValidator<FullName, String> {
    private static final String FULL_NAME_PATTERN = "[a-zA-Z가-힣]{1,20}";

    @Override
    public void initialize(FullName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return FULL_NAME_PATTERN.matches(name);
    }
}

