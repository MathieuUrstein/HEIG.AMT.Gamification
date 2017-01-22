package ch.heigvd.gamification.validator;

import ch.heigvd.gamification.error.ErrorsCodes;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Used as a validator to ensure that every field in the specified DTO is present in the json.
 */
public class FieldsRequiredAndNotEmptyValidator implements Validator {
    private final Class dtoClass;

    /**
     * The DTO containing the fields that will be used to validate json.
     *
     * @param dtoClass The class od the DTO.
     */
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
                Method getter = pd.getReadMethod();
                String propertyName = pd.getName();

                // if a getter exists for this property
                if (getter != null && !"class".equals(propertyName)) {

                    // if property is optional
                    ApiModelProperty apiProp = getter.getAnnotation(ApiModelProperty.class);
                    if (apiProp != null && !apiProp.required()) {
                        continue;
                    }

                    // reject if field not present
                    if (getter.invoke(o) == null) {
                        errors.rejectValue(pd.getName(), ErrorsCodes.FIELD_REQUIRED, ErrorsCodes.FIELD_REQUIRED_MESSAGE);
                    }
                    // else rejects if field empty
                    else {
                        ValidationUtils.rejectIfEmpty(errors, pd.getName(), ErrorsCodes.FIELD_EMPTY,
                                ErrorsCodes.FIELD_EMPTY_MESSAGE);
                    }
                }
            }
        }
        catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
