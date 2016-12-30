package ch.heigvd.gamification.error;

import ch.heigvd.gamification.serializer.ErrorJSONFieldsContentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.validation.FieldError;

import java.util.LinkedList;
import java.util.List;

@JsonSerialize(using = ErrorJSONFieldsContentSerializer.class)
public class ErrorJSONFieldsContent {
    private List<FieldError> errors;

    public ErrorJSONFieldsContent() {
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
