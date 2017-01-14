package ch.heigvd.gamification.model;

import javax.persistence.*;

@Entity
@Table(name="event_rule", uniqueConstraints = @UniqueConstraint(columnNames =  {"name", "application_id"}))
public class EventRule extends Rule {
    // TODO annotations
    private String eventType;
    private PointScale pointScale;
    private int nPointsToGive;

    public EventRule() {}

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public PointScale getPointScale() {
        return pointScale;
    }

    public void setPointScale(PointScale pointScale) {
        this.pointScale = pointScale;
    }

    public int getnPointsToGive() {
        return nPointsToGive;
    }

    public void setnPointsToGive(int nPointsToGive) {
        this.nPointsToGive = nPointsToGive;
    }
}
