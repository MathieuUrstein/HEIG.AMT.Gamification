package ch.heigvd.gamification.error;

import ch.heigvd.gamification.serializer.ErrorBadRequestSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.validation.FieldError;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sebbos on 12.12.2016.
 */
@JsonSerialize(using = ErrorBadRequestSerializer.class)
public class ErrorBadRequest {
    private List<FieldError> errors;

    public ErrorBadRequest() {
        errors = new LinkedList<>();
    }

    public ErrorBadRequest(List<FieldError> errors) {
        this.errors = errors;
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
