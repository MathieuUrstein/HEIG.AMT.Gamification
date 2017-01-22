package ch.heigvd.gamification.model;

import ch.heigvd.gamification.dto.BadgeDTO;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "badge", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "application_id"}))
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @OneToMany(targetEntity = BadgeAward.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "badge")
    private List<BadgeAward> badgeAwards = new LinkedList<>();

    public Badge() {}

    private void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getId() {
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
    }

    public List<BadgeAward> getBadgeAwards() {
        return badgeAwards;
    }

    public void addPointAward(BadgeAward badgeAward) {
        badgeAwards.add(badgeAward);
    }

    public BadgeDTO toDTO() {
        return new BadgeDTO(name, image);
    }
}
