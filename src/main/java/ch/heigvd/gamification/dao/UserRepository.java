package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByApplicationNameAndUsername(String name, String username);
    Collection<User> findAllByApplicationName(String application);
}
