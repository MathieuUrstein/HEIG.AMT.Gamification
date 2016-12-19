package ch.heigvd.gamification.services;

import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.dao.UserRepository;
import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.Event;
import ch.heigvd.gamification.model.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventProcessor {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public EventProcessor(UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    @Async
    @Transactional
    public void processEvent(Application application, EventDTO eventDTO) {
        Optional<User> opt = userRepository.findByApplicationNameAndUsername(application.getName(), eventDTO.getUsername());

        User user;

        if (!opt.isPresent()) {
            System.out.println("user doesn't exist");

            Application app = applicationRepository.findByName(application.getName());
            user = new User();

            user.setUsername(eventDTO.getUsername());
            user.setApplication(application);
            app.addUser(user);
        }
        else {
            user = opt.get();
        }

        Event event = new Event();

        event.setType(eventDTO.getType());
        event.setUser(user);
        user.addEvent(event);

        userRepository.save(user);
    }

}
