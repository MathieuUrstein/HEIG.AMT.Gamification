package ch.heigvd.gamification.services;

import ch.heigvd.gamification.dao.UserRepository;
import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.Event;
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
    public void processEvent(Application application, EventDTO eventDTO) {
        User user = userRepository.findByApplicationNameAndUsername(application.getName(), eventDTO.getUsername());

        if (user == null) {
            System.out.println("null");

            user = new User();

            user.setUsername(eventDTO.getUsername());
            user.setApplication(application);
        }

        Event event = new Event();

        event.setType(eventDTO.getType());
        event.setUser(user);

        userRepository.save(user);
    }

}
