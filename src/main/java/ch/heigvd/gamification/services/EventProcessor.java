package ch.heigvd.gamification.services;

import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.dao.EventRuleRepository;
import ch.heigvd.gamification.dao.TriggerRuleRepository;
import ch.heigvd.gamification.dao.UserRepository;
import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventProcessor {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final EventRuleRepository eventRuleRepository;
    private final TriggerRuleRepository triggerRuleRepository;

    public EventProcessor(UserRepository userRepository, ApplicationRepository applicationRepository,
                          EventRuleRepository eventRuleRepository, TriggerRuleRepository triggerRuleRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.eventRuleRepository = eventRuleRepository;
        this.triggerRuleRepository = triggerRuleRepository;
    }

    @Async
    @Transactional
    public void processEvent(Application application, EventDTO eventDTO) {


        /*Event event = new Event();

        event.setType(eventDTO.getType());
        event.setUser(user);
        user.addEvent(event);

        userRepository.save(user);*/

        User u = getOrCreateUser(application, eventDTO.getUsername());

        // TODO apply rules
        // fetch rules
        // first, add points on the given point scale if there is a rule
        // second, award badges if needed
    }

    /**
     * Gets an User based on the application name and his username. Create him if he doesn't exist.
     *
     * @param application The application related to the user.
     * @param username The username of the user to get or create.
     * @return The retrieved or created user.
     */
    private User getOrCreateUser(Application application, String username) {
        final String appName = application.getName();
        Optional<User> opt = userRepository.findByApplicationNameAndUsername(appName, username);

        if (!opt.isPresent()) {
            System.out.println("user doesn't exist");

            Application app = applicationRepository.findByName(appName);
            User user = new User();

            user.setUsername(username);
            user.setApplication(application);
            app.addUser(user);

            return user;
        }

        return opt.get();
    }
}
