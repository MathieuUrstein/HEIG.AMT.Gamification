package ch.heigvd.gamification.error;

import ch.heigvd.gamification.serializer.ErrorBadRequestSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sebbos on 12.12.2016.
 */
@JsonSerialize(using = ErrorBadRequestSerializer.class)
public class ErrorBadRequest {
    private List<ErrorField> errors;

    public ErrorBadRequest() {
        errors = new LinkedList<>();
    }

    public ErrorBadRequest(List<ErrorField> errors) {
        this.errors = errors;
    }

    public void addError(ErrorField error) {
        errors.add(error);
    }

    public void setErrors(List<ErrorField> errors) {
        this.errors = errors;
    }

    public List<ErrorField> getErrors() {
        return errors;
    }
}
