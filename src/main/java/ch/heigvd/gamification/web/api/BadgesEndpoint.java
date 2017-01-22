package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.dao.BadgeRepository;
import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.exception.ConflictException;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.Badge;
import ch.heigvd.gamification.util.URIs;
import ch.heigvd.gamification.validator.FieldsRequiredAndNotEmptyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(URIs.BADGES)
public class BadgesEndpoint implements BadgesApi {
    private final BadgeRepository badgeRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public BadgesEndpoint(BadgeRepository badgeRepository, ApplicationRepository applicationRepository) {
        this.badgeRepository = badgeRepository;
        this.applicationRepository = applicationRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(BadgeDTO.class));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BadgeDTO> getBadges(@ApiIgnore @RequestAttribute("application") Application app) {
        return badgeRepository.findByApplicationName(app.getName())
                .stream()
                .map(Badge::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{name}")
    public BadgeDTO getBadge(@ApiIgnore @RequestAttribute("application") Application app, @PathVariable String name) {
        Badge badge = badgeRepository
                .findByApplicationNameAndName(app.getName(), name)
                .orElseThrow(NotFoundException::new);

        return badge.toDTO();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createBadge(@ApiIgnore @RequestAttribute("application") Application application,
                                            @Valid @RequestBody BadgeDTO badgeDTO) {
        try {
            Application app = applicationRepository.findByName(application.getName());
            Badge badge = new Badge();

            badge.setName(badgeDTO.getName());
            badge.setApplication(app);
            app.addBadge(badge);

            badgeRepository.save(badge);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{name}/")
                    .buildAndExpand(badge.getName()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DataIntegrityViolationException e) {
            // The name of a badge must be unique in a gamified application
            throw new ConflictException("name");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{name}")
    public ResponseEntity<Void> completeUpdateBadge(@ApiIgnore @RequestAttribute("application") Application app,
                                                    @PathVariable String name, @Valid @RequestBody BadgeDTO badgeDTO) {
        try {
            Badge badge = badgeRepository
                    .findByApplicationNameAndName(app.getName(), name)
                    .orElseThrow(NotFoundException::new);

            badge.setName(badgeDTO.getName());

            badgeRepository.save(badge);

            return ResponseEntity.ok().build();
        }
        catch (DataIntegrityViolationException e) {
            // The name of a badge must be unique in a gamified application
            throw new ConflictException("name");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
    public ResponseEntity<Void> deleteBadge(@ApiIgnore @RequestAttribute("application") Application app,
                                            @PathVariable String name) {
        Badge badge = badgeRepository
                .findByApplicationNameAndName(app.getName(), name)
                .orElseThrow(NotFoundException::new);

        badgeRepository.delete(badge);

        return ResponseEntity.ok().build();
    }

}
