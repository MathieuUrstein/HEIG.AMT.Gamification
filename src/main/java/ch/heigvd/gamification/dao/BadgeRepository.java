package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.Badge;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends CrudRepository<Badge, Long> {
    List<Badge> findByApplicationName(String applicationName);
    Optional<Badge> findByApplicationNameAndName(String applicationName, String name);
}
