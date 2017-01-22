package ch.heigvd.gamification.error;

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

    // A field is unique in the JSON payload
    // You can't post two JSON payloads with the same value for this unique field
    String FIELD_UNIQUE = "3";
    String FIELD_UNIQUE_MESSAGE = "this field must be unique";

    // Application does not exist
    String APPLICATION_DOES_NOT_EXIST = "4";
    String APPLICATION_DOES_NOT_EXIST_MESSAGE = "application doesn't exist";

    // Authentication failed
    String AUTHENTICATION_FAILED = "5";
    String AUTHENTICATION_FAILED_MESSAGE = "authentication failed";

    // Authentication failed
    String ALREADY_AUTHENTICATED = "6";
    String ALREADY_AUTHENTICATED_MESSAGE = "already authenticated";

    // No token
    String NO_TOKEN = "7";
    String NO_TOKEN_MESSAGE = "no token";

    // JWT is not valid
    String JWT_INVALID = "8";
    String JWT_INVALID_MESSAGE = "JWT is invalid";

    // JWT format is not valid
    String INVALID_JWT_FORMAT = "9";
    String INVALID_JWT_FORMAT_MESSAGE = "invalid JWT format";
}
