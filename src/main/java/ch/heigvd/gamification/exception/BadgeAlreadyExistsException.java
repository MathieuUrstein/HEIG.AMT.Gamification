package ch.heigvd.gamification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by sebbos on 14.12.2016.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class BadgeAlreadyExistsException extends RuntimeException {
    public BadgeAlreadyExistsException(String name) {
        super("Badge with name '" + name + "' already exists.");
    }
}
