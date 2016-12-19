package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.RuleRepository;
import ch.heigvd.gamification.dto.RuleDTO;
import ch.heigvd.gamification.exception.ConflictException;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.Rule;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping(URIs.RULES)
public class RulesEndpoint {
    private final RuleRepository ruleRepository;

    @Autowired
    public RulesEndpoint(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(RuleDTO.class));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RuleDTO> getRules(@RequestAttribute("application") Application app) {
        return ruleRepository.findByApplicationName(app.getName())
                .stream()
                .map(this::toRuleDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{ruleId}")
    public RuleDTO getRule(@RequestAttribute("application") Application app, @PathVariable long ruleId) {
        Rule rule =  ruleRepository
                .findByApplicationNameAndId(app.getName(), ruleId)
                .orElseThrow(() -> new NotFoundException("rule", ruleId));

        return toRuleDTO(rule);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addRule(@Valid @RequestBody RuleDTO ruleDTO, @RequestAttribute("application") Application app) {
        try {
            Rule rule = new Rule();
            rule.setName(ruleDTO.getName());
            rule.setApplication(app);

            ruleRepository.save(rule);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(rule.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("rule", ruleDTO.getName());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{ruleId}")
    public ResponseEntity deleteRule(@RequestAttribute("application") Application app, @PathVariable long ruleId) {
        Rule rule = ruleRepository
                .findByApplicationNameAndId(app.getName(), ruleId)
                .orElseThrow(() -> new NotFoundException("rule", ruleId));

        ruleRepository.delete(rule);

        return ResponseEntity.ok().build();
    }

    private RuleDTO toRuleDTO(Rule rule) {
        return new RuleDTO(rule.getName());
    }
}
