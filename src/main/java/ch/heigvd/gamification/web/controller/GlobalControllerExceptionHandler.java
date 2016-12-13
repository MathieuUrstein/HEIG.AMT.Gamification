package ch.heigvd.gamification.web.controller;

import ch.heigvd.gamification.error.ErrorBadRequest;
import ch.heigvd.gamification.error.ErrorField;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Created by sebbos on 13.12.2016.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody ErrorBadRequest handleBadRequest(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ErrorBadRequest errorBadRequest = new ErrorBadRequest();

        for (FieldError fieldError : fieldErrors) {
            Pair<String, String > error = Pair.of(fieldError.getCode(), fieldError.getDefaultMessage());

            errorBadRequest.addError(new ErrorField(fieldError.getField(), error));
        }

        return errorBadRequest;
    }
}
