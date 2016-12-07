package ch.heigvd.gamification.web;

import ch.heigvd.gamification.exception.BadgeNotFoundException;
import ch.heigvd.gamification.model.Badge;
import ch.heigvd.gamification.model.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by sebbos on 06.12.2016.
 */
@RestController
@RequestMapping("/badges")
class BadgeRestController {

    private final BadgeRepository badgeRepository;

    @Autowired
    BadgeRestController(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{badgeId}")
    Badge getBadge(@PathVariable Long badgeId) {
        return this.badgeRepository.findById(badgeId).orElseThrow(
                () -> new BadgeNotFoundException(badgeId)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addBadge(@RequestBody Badge input) {
        Badge result = badgeRepository.save(new Badge(input.getName(), input.getImage()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
