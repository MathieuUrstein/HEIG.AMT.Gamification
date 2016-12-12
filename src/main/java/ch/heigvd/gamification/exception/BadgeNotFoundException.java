package ch.heigvd.gamification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by sebbos on 07.12.2016.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BadgeNotFoundException extends RuntimeException {
    public BadgeNotFoundException(Long badgeId) {
        super("Could not find badge with id '" + badgeId + "'.");
    }
}
