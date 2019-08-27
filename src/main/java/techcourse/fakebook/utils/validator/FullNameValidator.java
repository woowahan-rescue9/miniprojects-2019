package techcourse.fakebook.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class FullNameValidator implements ConstraintValidator<FullName, String> {
    private static final Pattern FULL_NAME_PATTERN = Pattern.compile("[a-zA-Z가-힣]{1,20}");

    @Override
    public void initialize(FullName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return FULL_NAME_PATTERN.matcher(name).matches();
    }
}

