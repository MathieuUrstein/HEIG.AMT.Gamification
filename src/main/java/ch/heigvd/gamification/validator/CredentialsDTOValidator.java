package ch.heigvd.gamification.validator;

import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.dto.CredentialsDTO;
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
        ValidationUtils.rejectIfEmpty(errors, "name", "42"); // FIXME error code
        ValidationUtils.rejectIfEmpty(errors, "password", "42"); // FIXME error code
    }
}
