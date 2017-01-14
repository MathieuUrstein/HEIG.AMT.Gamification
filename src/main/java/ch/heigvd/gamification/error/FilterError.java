package ch.heigvd.gamification.error;

public class FilterError {
    private String message;

    public FilterError() {}

    public FilterError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
