package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.PointAward;
import ch.heigvd.gamification.model.PointScale;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PointAwardRepository extends CrudRepository<PointAward, Long> {
    List<PointAward> findByUserApplicationNameAndUserUsername(String applicationName, String username);
}
