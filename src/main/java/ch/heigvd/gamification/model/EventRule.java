package ch.heigvd.gamification.model;

import javax.persistence.*;

@Entity
@Table(name="event_rule", uniqueConstraints = @UniqueConstraint(columnNames =  {"name", "application_id"}))
public class EventRule extends Rule {
    // TODO annotations
    private String event;
    private PointScale pointScale;
    private int pointsGiven;

    public EventRule() {}

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public PointScale getPointScale() {
        return pointScale;
    }

    public void setPointScale(PointScale pointScale) {
        this.pointScale = pointScale;
    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    public void setPointsGiven(int pointsGiven) {
        this.pointsGiven = pointsGiven;
    }
}
