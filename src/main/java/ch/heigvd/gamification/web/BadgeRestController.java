package ch.heigvd.gamification.web;

import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.exception.BadgeNotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.Badge;
import ch.heigvd.gamification.model.BadgeRepository;
import ch.heigvd.gamification.validator.BadgeDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/badges")
class BadgeRestController {
    // TODO : endpoint for images (badges)

    private final BadgeRepository badgeRepository;

    @Autowired
    BadgeRestController(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.setValidator(new BadgeDTOValidator());
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Badge> getBadges() {
        return this.badgeRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{badgeId}")
    Badge getBadge(@PathVariable Long badgeId) {
        return this.badgeRepository.findById(badgeId).orElseThrow(
                () -> new BadgeNotFoundException(badgeId)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity addBadge(@Valid @RequestBody BadgeDTO badgeDTO, ServletRequest request) {
        Application app = (Application)request.getAttribute("application");
        System.out.println(app.getName());

        // TODO : image with a url
        Badge result = badgeRepository.save(new Badge(badgeDTO.getName(), badgeDTO.getImage()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{badgeId}")
    ResponseEntity deleteBadge(@PathVariable Long badgeId) {
        this.badgeRepository.delete(badgeId);
        return ResponseEntity.noContent().build();
    }
}
