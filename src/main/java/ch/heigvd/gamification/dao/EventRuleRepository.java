package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.EventRule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventRuleRepository extends CrudRepository<EventRule, Long> {
    Optional<EventRule> findByApplicationNameAndId(String applicationName, long ruleId);
    List<EventRule> findByApplicationName(String applicationName);
}
