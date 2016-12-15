package ch.heigvd.gamification.error;

/**
 * Created by sebbos on 12.12.2016.
 */
public interface ErrorsCodes {
    // A field in the JSON payload is required
    // This field must not be null
    String FIELD_REQUIRED = "0";
    String FIELD_REQUIRED_MESSAGE = "this field is required";

    // A field in the JSON payload must not be empty
    String FIELD_EMPTY = "1";
    String FIELD_EMPTY_MESSAGE = "this field can not be empty";

    // The JSON payload is malformed
    String MALFORMED_JSON = "2";
    String MALFORMED_JSON_MESSAGE = "malformed JSON";
}
