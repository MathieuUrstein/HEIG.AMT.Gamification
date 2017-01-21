package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.TriggerRule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TriggerRuleRepository extends CrudRepository<TriggerRule, Long> {
    Optional<TriggerRule> findByApplicationNameAndId(String applicationName, long ruleId);
    Optional<TriggerRule> findByApplicationNameAndName(String applicationName, String name);
    List<TriggerRule> findByApplicationName(String applicationName);
}
