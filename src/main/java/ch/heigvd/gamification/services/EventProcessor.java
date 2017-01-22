package ch.heigvd.gamification.services;

import ch.heigvd.gamification.dao.*;
import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.model.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventProcessor {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final EventRuleRepository eventRuleRepository;
    private final TriggerRuleRepository triggerRuleRepository;
    private final PointAwardRepository pointAwardRepository;
    private final BadgeAwardRepository badgeAwardRepository;

    public EventProcessor(UserRepository userRepository, ApplicationRepository applicationRepository,
                          EventRuleRepository eventRuleRepository, TriggerRuleRepository triggerRuleRepository,
                          PointAwardRepository pointAwardRepository, BadgeAwardRepository badgeAwardRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.eventRuleRepository = eventRuleRepository;
        this.triggerRuleRepository = triggerRuleRepository;
        this.badgeAwardRepository = badgeAwardRepository;
        this.pointAwardRepository = pointAwardRepository;
    }

    @Async
    @Transactional
    public void processEvent(Application application, EventDTO eventDTO) {
        // Here we could save the event if we wanted to do something later with it

        User u = getOrCreateUser(application, eventDTO.getUsername());
        givePoints(u, application.getName(), eventDTO.getType());
        awardBadges(u, application.getName());
    }

    /**
     * Give points according to event rules, if a given rule matches the current event.
     *
     * @param u The user to whom give points.
     * @param appName The name of the application containing rules.
     * @param event The current event type.
     */
    private void givePoints(User u, String appName, String event) {
        for (EventRule er : eventRuleRepository.findByApplicationName(appName)) {
            if (er.getEvent().equals(event)) {
                createPointAward(u, er);
            }
        }
    }

    /**
     * Gives points to an user on a given point scale.
     *
     * @param user The user to whom give the points.
     * @param eventRule The event rule defining how many points to give on which point scale.
     */
    private void createPointAward(User user, EventRule eventRule) {
        PointAward pa = new PointAward();
        pa.setUser(user);
        pa.setPointScale(eventRule.getPointScale());
        pa.setPoints(eventRule.getPointsGiven());
        user.addPointAward(pa);

        pointAwardRepository.save(pa);
    }

    /**
     * Awards badges according to the trigger rules existing.
     *
     * @param user The user to whom award badges.
     * @param appName The name of the application related to the user.
     */
    private void awardBadges(User user, String appName) {
        List<BadgeAward> badgeAwards = badgeAwardRepository
                .findByUserApplicationNameAndUserUsername(appName, user.getUsername());
        Map<PointScale, Integer> sumPointsByScale = computeSumPointsByScale(appName, user.getUsername());

        for (TriggerRule tr : triggerRuleRepository.findByApplicationName(appName)) {
            int totPointsReceived = sumPointsByScale.get(tr.getPointScale());
            boolean above = tr.getAboveLimit();
            if ((above && totPointsReceived >= tr.getLimit()) || (!above && totPointsReceived <= tr.getLimit())) {
                createBadgeAwardIfNotOwned(user, tr.getBadgeAwarded(), badgeAwards);
            }
        }
    }

    /**
     * Computes the sum of each point award for a given point scale for an user in an application.
     *
     * @param appName The name of the application.
     * @param username The username of the user.
     * @return A map with as key the point scale and as value the sum of the points for this user.
     */
    private Map<PointScale, Integer> computeSumPointsByScale(String appName, String username) {
        List<PointAward> pointAwards = pointAwardRepository
                .findByUserApplicationNameAndUserUsername(appName, username);

        HashMap<PointScale, Integer> sumPointsByScale = new HashMap<>();
        pointAwards.forEach(pa -> {
            PointScale key = pa.getPointScale();
            int points = (sumPointsByScale.containsKey(key) ? sumPointsByScale.get(key) : 0) + pa.getPoints();
            sumPointsByScale.put(key, points);
        });

        return sumPointsByScale;
    }

    /**
     * Awards a badge to the given user if he doesn't have it already.
     *
     * @param user The user to whom award the badge.
     * @param badge The badge to award.
     * @param badgeAwards The badges awards.
     */
    private void createBadgeAwardIfNotOwned(User user, Badge badge, List<BadgeAward> badgeAwards) {
        for (BadgeAward ba : badgeAwards) {
            if (ba.getBadge() == badge) {
                return;
            }
        }

        BadgeAward ba = new BadgeAward();
        ba.setUser(user);
        ba.setBadge(badge);
        badgeAwardRepository.save(ba);
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
            userRepository.save(user);

            return user;
        }

        return opt.get();
    }
}
