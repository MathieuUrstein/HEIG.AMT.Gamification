package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A rule applied on point scale change.")
public class TriggerRuleDTO extends RuleDTO {
    private String badgeAwarded;
    private String pointScale;
    private Integer limit;
    private Boolean aboveLimit;

    public TriggerRuleDTO() {
    }

    public TriggerRuleDTO(String name, String badgeAwarded, String pointScale, Integer limit, Boolean aboveLimit) {
        super(name);
        this.badgeAwarded = badgeAwarded;
        this.pointScale = pointScale;
        this.limit = limit;
        this.aboveLimit = aboveLimit;
    }

    @ApiModelProperty(value = "The badge awarded when rule condition matches.", required = true)
    public String getBadgeAwarded() {
        return badgeAwarded;
    }

    public void setBadgeAwarded(String badgeAwarded) {
        this.badgeAwarded = badgeAwarded;
    }

    @ApiModelProperty(value = "The point scale on which the rule is applied.", required = true)
    public String getPointScale() {
        return pointScale;
    }

    public void setPointScale(String pointScale) {
        this.pointScale = pointScale;
    }

    @ApiModelProperty(value = "The point limit to award the badge. The limit is inclusive.", required = true)
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @ApiModelProperty(value = "Whether the badge is awarded when current points is >= the limit or <= limit of points to reach",
            required = true)
    public Boolean getAboveLimit() {
        return aboveLimit;
    }

    public void setAboveLimit(Boolean aboveLimit) {
        this.aboveLimit = aboveLimit;
    }
}
