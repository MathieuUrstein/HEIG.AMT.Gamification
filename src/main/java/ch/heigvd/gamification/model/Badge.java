package ch.heigvd.gamification.model;

import javax.persistence.*;

/**
 * Created by sebbos on 07.12.2016.
 */
@Entity
@Table(name="badge")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name", unique = true, nullable = false, length = 60)
    private String name;

    @Column(name="image")
    private byte[] image;

    public Badge() {
    }

    public Badge(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }
}
