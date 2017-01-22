package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.EventRule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventRuleRepository extends CrudRepository<EventRule, Long> {
    Optional<EventRule> findByApplicationNameAndName(String applicationName, String name);
    List<EventRule> findByApplicationName(String applicationName);
}
