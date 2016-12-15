package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.UserDTO;
import ch.heigvd.gamification.exception.ConflictException;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.User;
import ch.heigvd.gamification.util.URIs;
import ch.heigvd.gamification.validator.UserDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(URIs.USERS)
public class UsersEndpoint {

    private final EndUserRepository endUserRepository;

    @Autowired
    UsersEndpoint(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new UserDTOValidator());
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<User> getUsers() {
        return endUserRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity createUser(@Valid @RequestBody UserDTO user) {
        try {
            User result = endUserRepository.save(new User(user.getUsername()));

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("user", user.getUsername());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    User getUser(@PathVariable Long id) {
        return endUserRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("user", id));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    ResponseEntity deleteUser(@PathVariable Long userId) {
        User user = endUserRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("userId", userId));

        endUserRepository.delete(user);

        return ResponseEntity.ok().build();
    }
}
