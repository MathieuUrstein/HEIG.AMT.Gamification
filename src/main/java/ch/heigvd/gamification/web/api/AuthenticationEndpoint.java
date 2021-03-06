package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.dto.CredentialsDTO;
import ch.heigvd.gamification.exception.ApplicationDoesNotExistException;
import ch.heigvd.gamification.exception.AuthenticationFailedException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.util.JWTUtils;
import ch.heigvd.gamification.util.PasswordUtils;
import ch.heigvd.gamification.util.URIs;
import ch.heigvd.gamification.validator.FieldsRequiredAndNotEmptyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(URIs.AUTH)
public class AuthenticationEndpoint implements AuthenticationApi {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public AuthenticationEndpoint(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(CredentialsDTO.class));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> login(@Valid @RequestBody CredentialsDTO credentials) {
        Application app = applicationRepository.findByName(credentials.getName());

        if (app == null) {
            throw new ApplicationDoesNotExistException();
        }

        if (!PasswordUtils.isPasswordValid(credentials.getPassword(), app.getPassword(), app.getSalt())) {
            throw new AuthenticationFailedException();
        }

        return ResponseEntity
                .ok()
                .header("Authorization", JWTUtils.generateToken(app.getName()))
                .build();
    }
}
