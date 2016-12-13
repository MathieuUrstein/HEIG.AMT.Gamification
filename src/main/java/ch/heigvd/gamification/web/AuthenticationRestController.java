package ch.heigvd.gamification.web;


import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.dto.CredentialsDTO;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.util.AuthenticationUtils;
import ch.heigvd.gamification.util.JWTUtils;
import ch.heigvd.gamification.util.URIs;
import ch.heigvd.gamification.validator.CredentialsDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(URIs.AUTH)
public class AuthenticationRestController {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public AuthenticationRestController(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.setValidator(new CredentialsDTOValidator());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody List<FieldError> processValidationError(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors(); // FIXME return custom Error
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(@Valid @RequestBody CredentialsDTO credentials) {
        Application app = applicationRepository.findByName(credentials.getApplicationName());

        if (app == null || !AuthenticationUtils.isPasswordValid(credentials.getPassword(), app.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return ResponseEntity
                .ok()
                .header("Authorization", JWTUtils.generateToken())
                .build();
    }
}
