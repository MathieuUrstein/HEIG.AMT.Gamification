package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A rule applied on point scale change.")
public class TriggerRuleDTO extends RuleDTO {
    @ApiModelProperty(value = "The badge awarded when rule condition matched.", required = true)
    private String badgeAwarded;

    @ApiModelProperty(value = "The point scale on which the rule is applied.", required = true)
    private String pointScale;

    @ApiModelProperty(value = "The point limit to award the badge.", required = true)
    private int limit;

    @ApiModelProperty(value = "Whether the badge is given above or below point limit.", required = true)
    private boolean aboveLimit;

    public TriggerRuleDTO() {
    }

    public TriggerRuleDTO(String name) {
        super(name);
    }

    public String getBadgeAwarded() {
        return badgeAwarded;
    }

    public void setBadgeAwarded(String badgeAwarded) {
        this.badgeAwarded = badgeAwarded;
    }

    public String getPointScale() {
        return pointScale;
    }

    public void setPointScale(String pointScale) {
        this.pointScale = pointScale;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean getAboveLimit() {
        return aboveLimit;
    }

    public void setAboveLimit(boolean aboveLimit) {
        this.aboveLimit = aboveLimit;
    }
}
