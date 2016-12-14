package ch.heigvd.gamification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by sebbos on 14.12.2016.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    public ConflictException(String element, String name) {
        super(element + " with name '" + name + "' already exists");
    }
}
