package ch.heigvd.gamification.model;

import ch.heigvd.gamification.util.PasswordUtils;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Lob
    @Column(name = "password", nullable = false)
    private byte[] password;

    @Lob
    @Column(name = "salt", nullable = false)
    private byte[] salt;

    @OneToMany(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "application")
    private List<User> users = new LinkedList<>();

    @OneToMany(targetEntity = PointScale.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "application")
    private List<PointScale> pointScales = new LinkedList<>();

    @OneToMany(targetEntity = Badge.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "application")
    private List<Badge> badges = new LinkedList<>();

    @OneToMany(targetEntity = Rule.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "application")
    private List<Rule> rules = new LinkedList<>();

    public Application() {}

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.salt = PasswordUtils.generateSalt();
        this.password = PasswordUtils.hashPassword(password, salt);
    }

    public byte[] getSalt() {
        return salt;
    }

    private void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<PointScale> getPointScales() {
        return pointScales;
    }

    public void addPointScale(PointScale pointScale) {
        pointScales.add(pointScale);
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void addBadge(Badge badge) {
        badges.add(badge);
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void addRules(Rule rule) {
        rules.add(rule);
    }
}
