
package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.UserRepository;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.User;
import ch.heigvd.gamification.util.URIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(URIs.USERS)
public class UsersEndpoint {

    private final UserRepository userRepository;

    @Autowired
    UsersEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<User> getUsers(@RequestAttribute("application") Application app) {
        return userRepository.findAllByApplicationName(app.getName());
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    User getUser(@PathVariable String username, @RequestAttribute("application") Application app) {
        return userRepository
                .findByApplicationNameAndUsername(app.getName(), username)
                .orElseThrow(() -> new NotFoundException("user", username));
    }
}

