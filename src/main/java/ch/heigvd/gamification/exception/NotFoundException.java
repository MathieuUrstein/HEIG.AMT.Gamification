package ch.heigvd.gamification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by sebbos on 07.12.2016.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String element, Long id) {
        super("could not find " + element + " with id '" + id + "'");
    }
}
