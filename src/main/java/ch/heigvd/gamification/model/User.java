package ch.heigvd.gamification.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", nullable = false, length = 40)
    private String username;

    @OneToMany(targetEntity = Event.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "endUser")
    private List<Event> events = new LinkedList<>();

    public User() {}

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
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
}
