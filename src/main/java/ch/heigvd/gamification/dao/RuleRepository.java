package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.Rule;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by sebbos on 15.12.2016.
 */
public interface RuleRepository extends CrudRepository<Rule, Long> {
    Optional<Rule> findByApplicationNameAndId(String applicationName, long ruleId);
    Iterable<Rule> findByApplicationName(String applicationName);
}
