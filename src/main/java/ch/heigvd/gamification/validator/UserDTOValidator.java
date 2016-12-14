package ch.heigvd.gamification.validator;

import ch.heigvd.gamification.dto.UserDTO;
import ch.heigvd.gamification.error.ErrorsCodes;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserDTO userDTO = (UserDTO) o;

        if (userDTO.getUsername() == null) {
            errors.rejectValue("username", ErrorsCodes.FIELD_REQUIRED, ErrorsCodes.FIELD_REQUIRED_MESSAGE);
        }
        else {
            ValidationUtils.rejectIfEmpty(errors, "username", ErrorsCodes.FIELD_EMPTY, ErrorsCodes.FIELD_EMPTY_MESSAGE);
        }
    }
}
