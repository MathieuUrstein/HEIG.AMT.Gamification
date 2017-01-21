package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.*;
import ch.heigvd.gamification.dto.EventRuleDTO;
import ch.heigvd.gamification.dto.RuleDTO;
import ch.heigvd.gamification.dto.TriggerRuleDTO;
import ch.heigvd.gamification.exception.ConflictException;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.*;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(URIs.EVENT_RULES)
public class EventRulesEndpoint implements EventRulesAPi {
    private final EventRuleRepository eventRuleRepository;
    private final ApplicationRepository applicationRepository;
    private final PointScaleRepository pointScaleRepository;

    @Autowired
    public EventRulesEndpoint(EventRuleRepository eventRuleRepository, ApplicationRepository applicationRepository,
                              PointScaleRepository pointScaleRepository) {
        this.eventRuleRepository = eventRuleRepository;
        this.applicationRepository = applicationRepository;
        this.pointScaleRepository = pointScaleRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(EventRuleDTO.class));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<EventRuleDTO> getEventRules(@ApiIgnore @RequestAttribute("application") Application app) {
        return eventRuleRepository.findByApplicationName(app.getName())
                .stream()
                .map(this::toEventRuleDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public EventRuleDTO getEventRule(@ApiIgnore @RequestAttribute("application") Application app,
                                     @PathVariable long id) {
        EventRule rule =  eventRuleRepository
                .findByApplicationNameAndId(app.getName(), id)
                .orElseThrow(NotFoundException::new);

        return toEventRuleDTO(rule);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createEventRule(@ApiIgnore @RequestAttribute("application") Application application,
                                                @Valid @RequestBody EventRuleDTO ruleDTO) {
        try {
            Application app = applicationRepository.findByName(application.getName());
            Optional<PointScale> opt = pointScaleRepository
                    .findByApplicationNameAndName(application.getName(), ruleDTO.getName());

            if (!opt.isPresent()) {
                throw new NotFoundException();
            }

            EventRule rule = new EventRule();

            rule.setName(ruleDTO.getName());
            rule.setApplication(app);
            rule.setEvent(ruleDTO.getEvent());
            rule.setPointScale(opt.get());
            rule.setPointsGiven(ruleDTO.getPointsGiven());
            app.addRules(rule);

            eventRuleRepository.save(rule);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(rule.getId()).toUri();

            return ResponseEntity.created(location).build();
        }

        catch (DataIntegrityViolationException e) {
            // The name of a rule must be unique in a gamified application.
            throw new ConflictException("name");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteEventRule(@ApiIgnore @RequestAttribute("application") Application app,
                                                @PathVariable long id) {
        EventRule rule = eventRuleRepository
                .findByApplicationNameAndId(app.getName(), id)
                .orElseThrow(NotFoundException::new);

        eventRuleRepository.delete(rule);

        return ResponseEntity.ok().build();
    }

    private EventRuleDTO toEventRuleDTO(EventRule rule) {
        return new EventRuleDTO(rule.getName());
    }
}
