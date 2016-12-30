package ch.heigvd.gamification.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String field) {
        super(field);
    }
}
