package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.util.URIs;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(URIs.USERS)
public class UsersEndpoint {
    @RequestMapping(method = RequestMethod.GET)
    String read() {
        return "";
    }
}
