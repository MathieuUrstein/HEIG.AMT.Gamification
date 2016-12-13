package ch.heigvd.gamification.error;

import org.springframework.data.util.Pair;

/**
 * Created by sebbos on 13.12.2016.
 */
public class ErrorField {
    private String field;
    private Pair<String, String> error;

    public ErrorField() {
    }

    public ErrorField(String field, Pair<String, String> error) {
        this.field = field;
        this.error = error;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setError(Pair<String, String> error) {
        this.error = error;
    }

    public String getField() {
        return field;
    }

    public Pair<String, String> getError() {
        return error;
    }
}
