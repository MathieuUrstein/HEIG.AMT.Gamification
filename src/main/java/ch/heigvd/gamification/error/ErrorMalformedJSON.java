package ch.heigvd.gamification.error;

/**
 * Created by sebbos on 15.12.2016.
 */
public class ErrorMalformedJSON {
    private String code;
    private String message;

    public ErrorMalformedJSON() {
    }

    public ErrorMalformedJSON(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
