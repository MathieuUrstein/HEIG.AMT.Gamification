package ch.heigvd.gamification.serializer;

import ch.heigvd.gamification.error.ErrorBadRequest;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.validation.FieldError;

import java.io.IOException;

/**
 * Created by sebbos on 14.12.2016.
 */
public class ErrorBadRequestSerializer extends StdSerializer<ErrorBadRequest> {
    public ErrorBadRequestSerializer() {
        this(null);
    }

    public ErrorBadRequestSerializer(Class<ErrorBadRequest> t) {
        super(t);
    }

    @Override
    public void serialize(ErrorBadRequest errorBadRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        for (FieldError fieldError : errorBadRequest.getErrors()) {
            jsonGenerator.writeObjectFieldStart(fieldError.getField());
            jsonGenerator.writeStringField("code", fieldError.getCode());
            jsonGenerator.writeStringField("message", fieldError.getDefaultMessage());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndObject();
    }
}
