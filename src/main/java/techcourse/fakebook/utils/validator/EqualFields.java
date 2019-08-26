package techcourse.fakebook.utils.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EqualFieldsUserSignupRequestValidator.class)
public @interface EqualFields {
    String message() default "두 필드가 서로 다릅니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String baseField();

    String matchField();

    @Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = FullNameValidator.class)
    @interface FullName {
        String message() default "이름 형식이 올바르지 않습니다.";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = PartitialNameValidator.class)
    @interface PartitialName {
        String message() default "이름 형식이 올바르지 않습니다.";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    class FullNameValidator implements ConstraintValidator<FullName, String> {
        private static final Pattern FULL_NAME_PATTERN = Pattern.compile("[a-zA-Z가-힣]{1,20}");

        @Override
        public void initialize(FullName constraintAnnotation) {
        }

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context) {
            return FULL_NAME_PATTERN.matcher(name).find();
        }
    }

    class PartitialNameValidator implements ConstraintValidator<PartitialName, String> {
        private static final Pattern PARTITIAL_NAME_PATTERN = Pattern.compile("[a-zA-Z가-힣]{1,10}");

        @Override
        public void initialize(PartitialName constraintAnnotation) {
        }

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context) {
            return PARTITIAL_NAME_PATTERN.matcher(name).find();
        }
    }
}
