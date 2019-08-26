package techcourse.fakebook.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PartitialNameValidator implements ConstraintValidator<PartitialName, String> {
    private static final Pattern PARTITIAL_NAME_PATTERN = Pattern.compile("[a-zA-Z가-힣]{1,10}");

    @Override
    public void initialize(PartitialName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return PARTITIAL_NAME_PATTERN.matcher(name).find();
    }
}
