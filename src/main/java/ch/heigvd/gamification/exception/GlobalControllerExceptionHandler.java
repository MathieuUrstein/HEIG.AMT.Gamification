package ch.heigvd.gamification.exception;

import ch.heigvd.gamification.error.ErrorJSONFieldsContent;
import ch.heigvd.gamification.error.ErrorDescription;
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

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ErrorJSONFieldsContent handleBadRequest(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ErrorJSONFieldsContent errorJSONFieldsContent = new ErrorJSONFieldsContent();

        for (FieldError fieldError : fieldErrors) {
            errorJSONFieldsContent.addError(fieldError);
        }

        return errorJSONFieldsContent;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    ErrorDescription handleBadRequest(HttpMessageNotReadableException e) {
        return new ErrorDescription(ErrorsCodes.MALFORMED_JSON, ErrorsCodes.MALFORMED_JSON_MESSAGE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PointScaleNotFoundException.class)
    @ResponseBody
    ErrorDescription handleBadRequest(PointScaleNotFoundException e) {
        return new ErrorDescription(ErrorsCodes.POINT_SCALE_NOT_FOUND, ErrorsCodes.POINT_SCALE_NOT_FOUND_MESSAGE);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ApplicationDoesNotExistException.class)
    @ResponseBody
    ErrorDescription handleUnauthorized(ApplicationDoesNotExistException e) {
        return new ErrorDescription(ErrorsCodes.APPLICATION_DOES_NOT_EXIST, ErrorsCodes.APPLICATION_DOES_NOT_EXIST_MESSAGE);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseBody
    ErrorDescription handleUnauthorized(AuthenticationFailedException e) {
        return new ErrorDescription(ErrorsCodes.AUTHENTICATION_FAILED, ErrorsCodes.AUTHENTICATION_FAILED_MESSAGE);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    @ResponseBody
    ErrorJSONFieldsContent handleConflict(ConflictException e) {
        String[] codes = new String[1];
        codes[0] = ErrorsCodes.FIELD_UNIQUE;

        // The message of the exception contains the duplicated field.
        FieldError fieldError = new FieldError(e.getClass().getName(), e.getMessage(), null, false, codes, null, ErrorsCodes.FIELD_UNIQUE_MESSAGE);
        ErrorJSONFieldsContent errorJSONFieldsContent = new ErrorJSONFieldsContent();

        errorJSONFieldsContent.addError(fieldError);

        return errorJSONFieldsContent;
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                // We don't want to send information when for example a NotFoundException is raised.
                return null;
            }
        };
    }
}
