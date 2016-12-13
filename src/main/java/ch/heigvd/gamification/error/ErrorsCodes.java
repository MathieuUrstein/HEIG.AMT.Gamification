package ch.heigvd.gamification.error;

/**
 * Created by sebbos on 12.12.2016.
 */
public class ErrorsCodes {
    // A field in the JSON payload is required
    // This field must not be null
    public static final String FIELD_REQUIRED = "0";
    public static final String FIELD_REQUIRED_MESSAGE = "This field is required.";

    // A field in the JSON payload must not be empty
    public static final String FIELD_EMPTY = "1";
    public static final String FIELD_EMPTY_MESSAGE = "This field can not be empty.";
}
