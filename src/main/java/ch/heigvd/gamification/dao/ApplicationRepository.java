package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long> {
    Application findByName(String name);
}
