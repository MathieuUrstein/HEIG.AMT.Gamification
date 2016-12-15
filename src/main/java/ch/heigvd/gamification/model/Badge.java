package ch.heigvd.gamification.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="badge")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name", nullable = false, length = 60)
    private String name;

    @Column(name="image")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @JsonBackReference
    private Application application;

    @OneToMany(targetEntity = BadgeAward.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "badge")
    @JsonManagedReference
    private List<BadgeAward> badgeAwards = new LinkedList<>();


    public Badge() {}

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

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
        application.addBadge(this);
    }

    public List<BadgeAward> getBadgeAwards() {
        return badgeAwards;
    }

    public void addPointAward(BadgeAward badgeAward) {
        badgeAwards.add(badgeAward);
    }
}
