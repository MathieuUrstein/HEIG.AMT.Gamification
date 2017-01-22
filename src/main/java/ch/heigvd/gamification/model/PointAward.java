package ch.heigvd.gamification.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "point_award")
public class PointAward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "date", columnDefinition = "DATETIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @Column(name = "points", nullable = false)
    private int points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_scale_id", nullable = false)
    private PointScale pointScale;

    public PointAward() {}

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PointScale getPointScale() {
        return pointScale;
    }

    public void setPointScale(PointScale pointScale) {
        this.pointScale = pointScale;
    }
}
