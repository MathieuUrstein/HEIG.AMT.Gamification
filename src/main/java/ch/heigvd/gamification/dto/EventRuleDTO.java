package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A rule applied on event creation.")
public class EventRuleDTO extends RuleDTO {
    @ApiModelProperty(value = "The event on which the rule is applied.", required = true)
    private String event;

    @ApiModelProperty(value = "The point scale on which the rule is applied.", required = true)
    private String pointScale;

    @ApiModelProperty(value = "How many points are given on the point scale.", required = true)
    private int pointsGiven;

    public EventRuleDTO() {
    }

    public EventRuleDTO(String name) {
        super(name);
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
