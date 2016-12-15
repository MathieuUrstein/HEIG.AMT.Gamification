package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.BadgeRepository;
import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.exception.ConflictException;
import ch.heigvd.gamification.exception.NotFoundException;
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

import javax.validation.Valid;
import java.net.URI;

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
    public Iterable<Badge> getBadges(@RequestAttribute("application") Application app) {
        return badgeRepository.findByApplicationName(app.getName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{badgeId}")
    public Badge getBadge(@PathVariable Long badgeId) {
        return badgeRepository.
                findById(badgeId)
                .orElseThrow(() -> new NotFoundException("badge", badgeId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addBadge(@Valid @RequestBody BadgeDTO badgeDTO, @RequestAttribute("application") Application app) {
        // TODO : image with a url

        try {
            Badge badge = new Badge();
            badge.setName(badgeDTO.getName());
            badge.setImage(badgeDTO.getImage());
            badge.setApplication(app);

            badgeRepository.save(badge);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(badge.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("badge", badgeDTO.getName());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{badgeId}")
    public ResponseEntity deleteBadge(@PathVariable long badgeId) {
        Badge badge = badgeRepository.
                findById(badgeId)
                .orElseThrow(() -> new NotFoundException("badge", badgeId));

        badgeRepository.delete(badge);

        return ResponseEntity.ok().build();
    }
}
