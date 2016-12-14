package ch.heigvd.gamification.model;

import javax.persistence.*;

@Entity
@Table(name="application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // FIXME hash + salt
        this.password = password;
        this.salt = "TODO"; // FIXME
    }

    private String getSalt() {
        return salt;
    }

    private void setSalt(String salt) {
        this.salt = salt;
    }
}
