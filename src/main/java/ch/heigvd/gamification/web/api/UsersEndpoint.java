
package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.UserRepository;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.User;
import ch.heigvd.gamification.util.URIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
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
    Collection<User> getUsers(ServletRequest request) {
        return userRepository.findAllByApplicationName(((Application)request.getAttribute("application")).getName());
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    User getUser(@PathVariable String username, ServletRequest request) {
        return userRepository
                .findByApplicationNameAndUsername(username, ((Application)request.getAttribute("application")).getName())
                .orElseThrow(() -> new NotFoundException("user", username));
    }
}

