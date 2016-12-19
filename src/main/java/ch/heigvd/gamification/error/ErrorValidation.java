package ch.heigvd.gamification.error;

import ch.heigvd.gamification.serializer.ErrorValidationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.validation.FieldError;

import java.util.LinkedList;
import java.util.List;

@JsonSerialize(using = ErrorValidationSerializer.class)
public class ErrorValidation {
    private List<FieldError> errors;

    public ErrorValidation() {
        errors = new LinkedList<>();
    }

    public void addError(FieldError error) {
        errors.add(error);
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
