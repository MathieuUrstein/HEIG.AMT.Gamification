package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.PointScale;
import org.springframework.data.repository.CrudRepository;

public interface PointScaleRespository extends CrudRepository<PointScale, Long> {
    PointScale findByName(String name);
}
