package ch.heigvd.gamification.web;

import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.dto.CredentialsDTO;
import ch.heigvd.gamification.model.Application;
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
@RequestMapping(URIs.REGISTER)
public class RegisterEndpoint {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public RegisterEndpoint(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.setValidator(new CredentialsDTOValidator());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    List<FieldError> processValidationError(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors(); // FIXME return custom Error
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(@Valid @RequestBody CredentialsDTO credentials) {
        Application app = applicationRepository.findByName(credentials.getName());

        if (app != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build(); // FIXME error message ?
        }

        app = new Application();
        app.setName(credentials.getName());
        app.setPassword(credentials.getName()); // FIXME hash

        applicationRepository.save(app);
        System.out.println(applicationRepository.findByName(credentials.getName()));

        // FIXME url ?
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Authorization", JWTUtils.generateToken())
                .build();
    }
}
