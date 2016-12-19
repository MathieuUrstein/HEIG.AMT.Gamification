package ch.heigvd.gamification.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "point_scale", uniqueConstraints = @UniqueConstraint(columnNames =  {"name", "application_id"}))
public class PointScale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @OneToMany(targetEntity = PointAward.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pointScale")
    private List<PointAward> pointAwards = new LinkedList<>();

    public PointScale() {}

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

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
        application.addPointScale(this);
    }

    public List<PointAward> getPointAwards() {
        return pointAwards;
    }

    public void addPointAward(PointAward pointAward) {
        pointAwards.add(pointAward);
    }
}
