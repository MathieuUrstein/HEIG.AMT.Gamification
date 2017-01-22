package ch.heigvd.gamification.model;

import javax.persistence.*;

@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.JOINED)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "username", nullable = false)
    private String username;

    public Event() {
    }

    public Event(String type, String username) {
        this.type = type;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(User user) {
        this.username = username;
    }
}
