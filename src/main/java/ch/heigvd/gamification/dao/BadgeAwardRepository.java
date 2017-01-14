package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.BadgeAward;
import ch.heigvd.gamification.model.PointAward;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BadgeAwardRepository extends CrudRepository<BadgeAward, Long> {
    List<BadgeAward> findByUserApplicationNameAndUserUsername(String applicationName, String username);
}
