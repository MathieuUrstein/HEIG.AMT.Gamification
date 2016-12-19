package ch.heigvd.gamification.serializer;

import ch.heigvd.gamification.error.ErrorValidation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.validation.FieldError;

import java.io.IOException;

public class ErrorValidationSerializer extends StdSerializer<ErrorValidation> {
    public ErrorValidationSerializer() {
        this(null);
    }

    public ErrorValidationSerializer(Class<ErrorValidation> t) {
        super(t);
    }

    @Override
    public void serialize(ErrorValidation errorValidation, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        for (FieldError fieldError : errorValidation.getErrors()) {
            jsonGenerator.writeObjectFieldStart(fieldError.getField());
            jsonGenerator.writeStringField("code", fieldError.getCode());
            jsonGenerator.writeStringField("message", fieldError.getDefaultMessage());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndObject();
    }
}
