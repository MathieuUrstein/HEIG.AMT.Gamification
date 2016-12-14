package ch.heigvd.gamification.services;

import ch.heigvd.gamification.dao.UserRepository;
import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventProcessor {
    private final UserRepository userRepository;

    public EventProcessor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    @Transactional
    public void processEvent(Application application, EventDTO event) {
        // TODO : gestion concurrence => rejour requête

        User user = userRepository.findByApplicationNameAndUserName(application.getName(), event.getUserName());

        if (user == null) {
            user = new User();
            /*targetUser.setApplication(application);
            targetUser.setIdInGamifiedApplication(event.getUserId());
            targetUser.setNumberOfEvents(1);
            endUsersRepository.save(targetUser);*/
        }
        else {
            //targetUser.setNumberOfEvents(targetUser.getNumberOfEvents()+1);
        }

    }

}
