package techcourse.fakebook.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class EqualFieldsValidator<T> implements ConstraintValidator<EqualFields, T> {
    private String baseField;
    private String matchField;

    @Override
    public void initialize(EqualFields constraintAnnotation) {
        baseField = constraintAnnotation.baseField();
        matchField = constraintAnnotation.matchField();
    }

    @Override
    public boolean isValid(T request, ConstraintValidatorContext context) {
        try {
            Object baseFieldValue = getFieldValue(request, baseField);
            Object matchFieldValue = getFieldValue(request, matchField);

            if (!baseFieldValue.equals(matchFieldValue)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(getMessage())
                        .addPropertyNode(matchField)
                        .addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getMessage() {
        return "두 필드가 다릅니다";
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> c = object.getClass();
        Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
