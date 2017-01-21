package ch.heigvd.gamification.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "application_id"}))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @OneToMany(targetEntity = Event.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Event> events = new LinkedList<>();

    @OneToMany(targetEntity = BadgeAward.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<BadgeAward> badgeAwards = new LinkedList<>();

    @OneToMany(targetEntity = PointAward.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")

    private List<PointAward> pointAwards = new LinkedList<>();

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<BadgeAward> getBadgeAwards() {
        return badgeAwards;
    }

    public void addBadgeAward(BadgeAward badgeAward) {
        badgeAwards.add(badgeAward);
    }

    public List<PointAward> getPointAwards() {
        return pointAwards;
    }

    public void addPointAward(PointAward pointAward) {
        pointAwards.add(pointAward);
    }
}
