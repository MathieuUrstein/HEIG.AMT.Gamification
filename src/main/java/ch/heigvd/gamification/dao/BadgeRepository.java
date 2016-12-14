package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.Badge;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by sebbos on 07.12.2016.
 */
public interface BadgeRepository extends CrudRepository<Badge, Long> {
    Optional<Badge> findById(long badgeId);
}
