package ch.heigvd.gamification.validator;

import ch.heigvd.gamification.dto.BadgeDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by sebbos on 12.12.2016.
 */
public class BadgeDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return BadgeDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
    }
}
