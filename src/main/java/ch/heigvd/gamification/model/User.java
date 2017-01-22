package ch.heigvd.gamification.model;

import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.dto.PointsOnPointScaleDTO;
import ch.heigvd.gamification.dto.UserDTO;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public UserDTO toDTO() {
        List<BadgeDTO> badges = badgeAwards
                .stream()
                .map(ba -> ba.getBadge().toDTO())
                .collect(Collectors.toList());

        Map<String, Integer> pointsOnPointScales = new HashMap<>();
        for (PointAward pa: pointAwards) {
            String key = pa.getPointScale().getName();
            int points = pointsOnPointScales.containsKey(key) ? pointsOnPointScales.get(key) : 0;
            pointsOnPointScales.put(key, points + pa.getPoints());
        }

        List<PointsOnPointScaleDTO> points = pointsOnPointScales.entrySet()
                .stream()
                .map(i -> new PointsOnPointScaleDTO(i.getKey(), i.getValue()))
                .collect(Collectors.toList());

        UserDTO res = new UserDTO();
        res.setUsername(username);
        res.setBadges(badges);
        res.setPoints(points);

        return res;
    }
}
