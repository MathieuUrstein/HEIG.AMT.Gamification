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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(URIs.TRIGGER_RULES)
public class TriggerRulesEndpoint {
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
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(RuleDTO.class));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RuleDTO> getRules(@RequestAttribute("application") Application app) {
        return triggerRuleRepository.findByApplicationName(app.getName())
                .stream()
                .map(this::toRuleDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{ruleId}")
    public RuleDTO getRule(@RequestAttribute("application") Application app, @PathVariable long ruleId) {
        Rule rule =  triggerRuleRepository
                .findByApplicationNameAndId(app.getName(), ruleId)
                .orElseThrow(NotFoundException::new);

        return toRuleDTO(rule);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addTriggerRule(@Valid @RequestBody TriggerRuleDTO ruleDTO,
                                         @RequestAttribute("application") Application application) {
        try {
            Application app = applicationRepository.findByName(application.getName());
            Optional<PointScale> optPS = pointScaleRepository
                    .findByApplicationNameAndName(application.getName(), ruleDTO.getName());

            if (!optPS.isPresent()) {
                throw new NotFoundException();
            }

            Optional<Badge> optBadge = badgeRepository
                    .findByApplicationNameAndName(application.getName(), ruleDTO.getBadgeAwarded());

            if (!optBadge.isPresent()) {
                throw new NotFoundException();
            }

            TriggerRule rule = new TriggerRule();

            rule.setName(ruleDTO.getName());
            rule.setApplication(app);
            rule.setBadgeAwarded(optBadge.get());
            rule.setPointScale(optPS.get());
            rule.setLimit(ruleDTO.getLimit());
            rule.setAboveLimit(ruleDTO.getAboveLimit());

            app.addRules(rule);

            triggerRuleRepository.save(rule);

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

    @RequestMapping(method = RequestMethod.DELETE, value = "/{ruleName}")
    public ResponseEntity deleteTriggerRule(@RequestAttribute("application") Application app,
                                            @PathVariable String ruleName) {
        TriggerRule rule = triggerRuleRepository
                .findByApplicationNameAndName(app.getName(), ruleName)
                .orElseThrow(NotFoundException::new);

        triggerRuleRepository.delete(rule);

        return ResponseEntity.ok().build();
    }

    private RuleDTO toRuleDTO(Rule rule) {
        return new RuleDTO(rule.getName());
    }
}
