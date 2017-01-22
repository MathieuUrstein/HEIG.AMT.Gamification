package ch.heigvd.gamification.model;

import javax.persistence.*;

@Entity
@Table(name = "event_rule")
@PrimaryKeyJoinColumn(name = "id")
public class EventRule extends Rule {
    @Column(name = "event", nullable = false)
    private String event;

    @Column(name = "points_given", nullable = false)
    private int pointsGiven;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_scale_id", nullable = false)
    private PointScale pointScale;

    public EventRule() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    public void setPointsGiven(int pointsGiven) {
        this.pointsGiven = pointsGiven;
    }

    public PointScale getPointScale() {
        return pointScale;
    }

    public void setPointScale(PointScale pointScale) {
        this.pointScale = pointScale;
    }
}
