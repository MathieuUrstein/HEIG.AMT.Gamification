package ch.heigvd.gamification.dao;

import ch.heigvd.gamification.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sebbos on 14.12.2016.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByApplicationName(String applicationName, String userName);
}
