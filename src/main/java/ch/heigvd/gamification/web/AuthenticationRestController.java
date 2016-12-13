package ch.heigvd.gamification.web;


import ch.heigvd.gamification.dto.CredentialsDTO;
import ch.heigvd.gamification.util.AuthenticationUtils;
import ch.heigvd.gamification.util.JWTUtils;
import ch.heigvd.gamification.util.URIs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(URIs.AUTH)
public class AuthenticationRestController {
    public ResponseEntity login(@RequestBody CredentialsDTO credentials) {

        // TODO request app from DB
        if (!AuthenticationUtils.isPasswordValid(credentials.getPassword(), "")) {
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
