package techcourse.fakebook.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotExistsEmailValidator.class)
public @interface NotExistsEmail {
    String message() default "유효하지 않은 이메일 입니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
