package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.Badge;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface BadgeRepository extends CrudRepository<Badge, Long> {
    Optional<Badge> findByApplicationNameAndId(String name, long badgeId);

    Iterable<Badge> findByApplicationName(String name);
}
