package ch.heigvd.gamification.serializer;

import ch.heigvd.gamification.error.ErrorJSONFieldsContent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.validation.FieldError;

import java.io.IOException;

public class ErrorJSONFieldsContentSerializer extends StdSerializer<ErrorJSONFieldsContent> {
    public ErrorJSONFieldsContentSerializer() {
        this(null);
    }

    public ErrorJSONFieldsContentSerializer(Class<ErrorJSONFieldsContent> t) {
        super(t);
    }

    @Override
    public void serialize(ErrorJSONFieldsContent errorJSONFieldsContent, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        for (FieldError fieldError : errorJSONFieldsContent.getErrors()) {
            jsonGenerator.writeObjectFieldStart(fieldError.getField());
            jsonGenerator.writeStringField("code", fieldError.getCode());
            jsonGenerator.writeStringField("message", fieldError.getDefaultMessage());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndObject();
    }
}
