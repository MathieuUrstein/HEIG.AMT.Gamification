package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByApplicationNameAndUsername(String name, String username);
    Iterable<User> findByApplicationName(String application);
}
