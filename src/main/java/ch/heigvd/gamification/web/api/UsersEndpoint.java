
package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.UserRepository;
import ch.heigvd.gamification.dto.UserDTO;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.User;
import ch.heigvd.gamification.util.URIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(URIs.USERS)
public class UsersEndpoint implements UsersApi {
    private final UserRepository userRepository;

    @Autowired
    public UsersEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserDTO> getUsers(@ApiIgnore @RequestAttribute("application") Application app) {
        return userRepository.findByApplicationName(app.getName())
                .stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public UserDTO getUser(@ApiIgnore @RequestAttribute("application") Application app,
                           @PathVariable String username) {
        User user = userRepository
                .findByApplicationNameAndUsername(app.getName(), username)
                .orElseThrow(NotFoundException::new);

        return toUserDTO(user);
    }

    private UserDTO toUserDTO(User user) {
        return new UserDTO(user.getUsername());
    }
}

