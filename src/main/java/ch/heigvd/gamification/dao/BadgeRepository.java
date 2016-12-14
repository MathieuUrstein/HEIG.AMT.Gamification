package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by sebbos on 07.12.2016.
 */
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findById(Long badgeId);
}
