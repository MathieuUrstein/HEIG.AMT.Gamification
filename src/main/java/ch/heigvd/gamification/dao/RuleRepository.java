package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.Rule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RuleRepository extends CrudRepository<Rule, Long> {
    Optional<Rule> findByApplicationNameAndId(String applicationName, long ruleId);
    List<Rule> findByApplicationName(String applicationName);
}
