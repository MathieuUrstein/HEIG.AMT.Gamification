package ch.heigvd.gamification.exception;

import ch.heigvd.gamification.error.ErrorMalformedJSON;
import ch.heigvd.gamification.error.ErrorValidation;
import ch.heigvd.gamification.error.ErrorsCodes;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody ErrorValidation handleBadRequest(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ErrorValidation errorValidation = new ErrorValidation();

        for (FieldError fieldError : fieldErrors) {
            errorValidation.addError(fieldError);
        }

        return errorValidation;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody ErrorMalformedJSON handleBadRequest(HttpMessageNotReadableException e) {
        return new ErrorMalformedJSON(ErrorsCodes.MALFORMED_JSON, ErrorsCodes.MALFORMED_JSON_MESSAGE);
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
