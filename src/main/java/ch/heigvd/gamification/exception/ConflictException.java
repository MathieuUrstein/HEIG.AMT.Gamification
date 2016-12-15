package ch.heigvd.gamification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    public ConflictException(String element, String name) {
        super(String.format("%s with name %s already exists", element, name));
    }
}
