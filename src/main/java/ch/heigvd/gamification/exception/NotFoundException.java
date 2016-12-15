package ch.heigvd.gamification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String element, Long id) {
        super(String.format("could not find '%s' with id '%d'", element, id));
    }

    public NotFoundException(String element, String name) {
        super(String.format("could not find '%s' with name '%s'", element, name));
    }
}
