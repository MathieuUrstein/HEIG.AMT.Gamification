package ch.heigvd.gamification.validator;

import ch.heigvd.gamification.error.ErrorsCodes;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class FieldsRequiredAndNotEmptyValidator implements Validator {
    private final Class dtoClass;

    public FieldsRequiredAndNotEmptyValidator(Class dtoClass) {
        this.dtoClass = dtoClass;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return dtoClass.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(dtoClass).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {

                    if (pd.getReadMethod().invoke(o) == null) {
                        errors.rejectValue(pd.getName(), ErrorsCodes.FIELD_REQUIRED, ErrorsCodes.FIELD_REQUIRED_MESSAGE);
                    } else {
                        ValidationUtils.rejectIfEmpty(errors, pd.getName(), ErrorsCodes.FIELD_EMPTY,
                                ErrorsCodes.FIELD_EMPTY_MESSAGE);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
