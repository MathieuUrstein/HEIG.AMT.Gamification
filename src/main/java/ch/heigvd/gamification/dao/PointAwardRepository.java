package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.PointAward;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointAwardRepository extends CrudRepository<PointAward, Long> {
    List<PointAward> findByUserApplicationNameAndUserUsername(String applicationName, String username);
}
