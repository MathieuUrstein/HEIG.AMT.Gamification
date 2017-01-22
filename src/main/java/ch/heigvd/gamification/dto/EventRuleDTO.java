package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A rule applied on event creation.")
public class EventRuleDTO extends RuleDTO {
    private String event;
    private String pointScale;
    private Integer pointsGiven;

    public EventRuleDTO() {
    }

    public EventRuleDTO(String name) {
        super(name);
    }

    @ApiModelProperty(value = "The event on which the rule is applied.", required = true)
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @ApiModelProperty(value = "The point scale on which the rule is applied.", required = true)
    public String getPointScale() {
        return pointScale;
    }

    public void setPointScale(String pointScale) {
        this.pointScale = pointScale;
    }

    @ApiModelProperty(value = "How many points are given on the point scale.", required = true)
    public Integer getPointsGiven() {
        return pointsGiven;
    }

    public void setPointsGiven(Integer pointsGiven) {
        this.pointsGiven = pointsGiven;
    }
}
