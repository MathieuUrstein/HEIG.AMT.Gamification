package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.PointScale;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PointScaleRepository extends CrudRepository<PointScale, Long> {
    Optional<PointScale> findByApplicationNameAndId(String applicationName, long id);
    Iterable<PointScale> findByApplicationName(String applicationName);
}