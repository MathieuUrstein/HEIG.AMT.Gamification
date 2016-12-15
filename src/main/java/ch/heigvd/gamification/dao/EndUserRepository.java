package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EndUserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
}
