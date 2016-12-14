package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.BadgeRepository;
import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.exception.BadgeAlreadyExistsException;
import ch.heigvd.gamification.exception.BadgeNotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.Badge;
import ch.heigvd.gamification.util.URIs;
import ch.heigvd.gamification.validator.BadgeDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

/**
 * Created by sebbos on 06.12.2016.
 */
@RestController
@RequestMapping(URIs.BADGES)
public class BadgesEndpoint {
    // TODO : endpoint for images (badges)

    private final BadgeRepository badgeRepository;

    @Autowired
    public BadgesEndpoint(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new BadgeDTOValidator());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Badge> getBadges() {
        return badgeRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{badgeId}")
    public Badge getBadge(@PathVariable Long badgeId) {
        return badgeRepository.findById(badgeId).orElseThrow(
                () -> new BadgeNotFoundException(badgeId)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addBadge(@Valid @RequestBody BadgeDTO badgeDTO, ServletRequest request) {
        Application app = (Application)request.getAttribute("application");
        System.out.println(app.getName());

        // TODO : image with a url

        try {
            Badge result = badgeRepository.save(new Badge(badgeDTO.getName(), badgeDTO.getImage()));

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DataIntegrityViolationException e) {
            throw new BadgeAlreadyExistsException(badgeDTO.getName());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{badgeId}")
    public ResponseEntity deleteBadge(@PathVariable Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId).orElseThrow(
                () -> new BadgeNotFoundException(badgeId)
        );

        badgeRepository.delete(badge);

        return ResponseEntity.ok().build();
    }
}
