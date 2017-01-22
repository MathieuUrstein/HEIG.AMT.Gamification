package ch.heigvd.gamification.validator;


import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.error.ErrorsCodes;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Used as a validator for the fields of a badge in a json.
 */
public class BadgeDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return BadgeDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        BadgeDTO dto = (BadgeDTO) o;

        if (dto.getName() == null) {
            errors.rejectValue("name", ErrorsCodes.FIELD_REQUIRED, ErrorsCodes.FIELD_REQUIRED_MESSAGE);
        }
        else {
            ValidationUtils.rejectIfEmpty(errors, "name", ErrorsCodes.FIELD_EMPTY, ErrorsCodes.FIELD_EMPTY_MESSAGE);
        }
    }
}
