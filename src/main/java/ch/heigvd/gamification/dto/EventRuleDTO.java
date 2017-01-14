package ch.heigvd.gamification.dto;

public class EventRuleDTO extends RuleDTO {
    private String event;
    private String pointScale;
    private int pointsGiven;

    public EventRuleDTO() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPointScale() {
        return pointScale;
    }

    public void setPointScale(String pointScale) {
        this.pointScale = pointScale;
    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    public void setPointsGiven(int pointsGiven) {
        this.pointsGiven = pointsGiven;
    }
}
