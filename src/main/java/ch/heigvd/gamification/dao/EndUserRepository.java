package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.Badge;
import ch.heigvd.gamification.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EndUserRepository extends JpaRepository<EndUser, Long> {
    Optional<EndUser> findById(Long userId);
}
