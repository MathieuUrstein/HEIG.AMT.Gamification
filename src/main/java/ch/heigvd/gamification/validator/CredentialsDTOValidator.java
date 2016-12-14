package ch.heigvd.gamification.validator;

import ch.heigvd.gamification.dto.CredentialsDTO;
import ch.heigvd.gamification.error.ErrorsCodes;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CredentialsDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CredentialsDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CredentialsDTO credentialsDTO = (CredentialsDTO) o;

        if (credentialsDTO.getName() == null) {
            errors.rejectValue("name", ErrorsCodes.FIELD_REQUIRED, ErrorsCodes.FIELD_REQUIRED_MESSAGE);
        }
        else {
            ValidationUtils.rejectIfEmpty(errors, "name", ErrorsCodes.FIELD_EMPTY, ErrorsCodes.FIELD_EMPTY_MESSAGE);
        }

        if (credentialsDTO.getPassword() == null) {
            errors.rejectValue("password", ErrorsCodes.FIELD_REQUIRED, ErrorsCodes.FIELD_REQUIRED_MESSAGE);
        }
        else {
            ValidationUtils.rejectIfEmpty(errors, ErrorsCodes.FIELD_EMPTY, ErrorsCodes.FIELD_EMPTY_MESSAGE);
        }
    }
}
