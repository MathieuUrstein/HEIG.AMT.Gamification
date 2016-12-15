package ch.heigvd.gamification.validator;

import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.error.ErrorsCodes;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by sebbos on 14.12.2016.
 */
public class EventDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return EventDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        EventDTO eventDTO = (EventDTO) o;

        if (eventDTO.getType() == null) {
            errors.rejectValue("type", ErrorsCodes.FIELD_REQUIRED, ErrorsCodes.FIELD_REQUIRED_MESSAGE);
        }
        else {
            ValidationUtils.rejectIfEmpty(errors, "type", ErrorsCodes.FIELD_EMPTY, ErrorsCodes.FIELD_EMPTY_MESSAGE);
        }

        if (eventDTO.getUserName() == null) {
            errors.rejectValue("username", ErrorsCodes.FIELD_REQUIRED, ErrorsCodes.FIELD_REQUIRED_MESSAGE);
        }
        else {
            ValidationUtils.rejectIfEmpty(errors, "username", ErrorsCodes.FIELD_EMPTY, ErrorsCodes.FIELD_EMPTY_MESSAGE);
        }
    }
}
