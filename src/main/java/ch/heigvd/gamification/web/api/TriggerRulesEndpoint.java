package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.dao.BadgeRepository;
import ch.heigvd.gamification.dao.PointScaleRepository;
import ch.heigvd.gamification.dao.TriggerRuleRepository;
import ch.heigvd.gamification.dto.TriggerRuleDTO;
import ch.heigvd.gamification.exception.ConflictException;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.Badge;
import ch.heigvd.gamification.model.PointScale;
import ch.heigvd.gamification.model.TriggerRule;
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
@RequestMapping(URIs.TRIGGER_RULES)
public class TriggerRulesEndpoint implements TriggerRulesApi {
    private final TriggerRuleRepository triggerRuleRepository;
    private final ApplicationRepository applicationRepository;
    private final PointScaleRepository pointScaleRepository;
    private final BadgeRepository badgeRepository;

    @Autowired
    public TriggerRulesEndpoint(TriggerRuleRepository triggerRuleRepository,
                                ApplicationRepository applicationRepository, PointScaleRepository pointScaleRepository,
                                BadgeRepository badgeRepository) {
        this.triggerRuleRepository = triggerRuleRepository;
        this.applicationRepository = applicationRepository;
        this.pointScaleRepository = pointScaleRepository;
        this.badgeRepository = badgeRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(TriggerRuleDTO.class));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TriggerRuleDTO> getTriggerRules(@ApiIgnore @RequestAttribute("application") Application app) {
        return triggerRuleRepository.findByApplicationName(app.getName())
                .stream()
                .map(this::toTriggerRuleDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{name}")
    public TriggerRuleDTO getTriggerRule(@ApiIgnore @RequestAttribute("application") Application app,
                           @PathVariable String name) {
        TriggerRule rule =  triggerRuleRepository
                .findByApplicationNameAndName(app.getName(), name)
                .orElseThrow(NotFoundException::new);

        return toTriggerRuleDTO(rule);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createTriggerRule(@ApiIgnore @RequestAttribute("application") Application application,
                                               @Valid @RequestBody TriggerRuleDTO ruleDTO) {
        try {
            Application app = applicationRepository.findByName(application.getName());
            PointScale pointScale = pointScaleRepository
                    .findByApplicationNameAndName(application.getName(), ruleDTO.getName())
                    .orElseThrow(NotFoundException::new);

            Badge badge = badgeRepository
                    .findByApplicationNameAndName(application.getName(), ruleDTO.getBadgeAwarded())
                    .orElseThrow(NotFoundException::new);

            TriggerRule rule = new TriggerRule();

            rule.setName(ruleDTO.getName());
            rule.setApplication(app);
            rule.setBadgeAwarded(badge);
            rule.setPointScale(pointScale);
            rule.setLimit(ruleDTO.getLimit());
            rule.setAboveLimit(ruleDTO.getAboveLimit());

            app.addRules(rule);

            triggerRuleRepository.save(rule);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{name}/")
                    .buildAndExpand(rule.getName()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DataIntegrityViolationException e) {
            // The name of a rule must be unique in a gamified application
            throw new ConflictException("name");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
    public ResponseEntity<Void> deleteTriggerRule(@ApiIgnore @RequestAttribute("application") Application app,
                                                  @PathVariable String name) {
        TriggerRule rule = triggerRuleRepository
                .findByApplicationNameAndName(app.getName(), name)
                .orElseThrow(NotFoundException::new);

        triggerRuleRepository.delete(rule);

        return ResponseEntity.ok().build();
    }

    private TriggerRuleDTO toTriggerRuleDTO(TriggerRule rule) {
        return new TriggerRuleDTO(rule.getName());
    }
}
