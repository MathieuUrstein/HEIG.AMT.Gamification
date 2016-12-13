package ch.heigvd.gamification.serializer;

import ch.heigvd.gamification.error.ErrorBadRequest;
import ch.heigvd.gamification.error.ErrorField;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

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

        for (ErrorField errorField : errorBadRequest.getErrors()) {
            jsonGenerator.writeObjectFieldStart(errorField.getField());
            jsonGenerator.writeStringField("code", errorField.getError().getFirst());
            jsonGenerator.writeStringField("message", errorField.getError().getSecond());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndObject();
    }
}
