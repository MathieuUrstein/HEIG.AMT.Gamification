package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByApplicationNameAndUsername(String name, String username);
    List<User> findByApplicationName(String application);
}
