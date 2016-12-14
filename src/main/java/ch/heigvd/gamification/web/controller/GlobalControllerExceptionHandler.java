package ch.heigvd.gamification.web.controller;

import ch.heigvd.gamification.error.ErrorBadRequest;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            errorBadRequest.addError(fieldError);
        }

        return errorBadRequest;
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, false);
                Map<String, Object> error = new HashMap<>();

                // We don't need other unusable info
                error.put("path", errorAttributes.get("path"));
                error.put("message", errorAttributes.get("message"));

                return error;
            }
        };
    }
}
