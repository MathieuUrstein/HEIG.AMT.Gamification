package ch.heigvd.gamification.dto;

import ch.heigvd.gamification.util.AwardLimitContext;

public class TriggerRuleDTO extends RuleDTO {
    private String badgeAwarded;
    private String pointScale;
    private int limit;
    private AwardLimitContext context;

    public TriggerRuleDTO() {
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

    public AwardLimitContext getContext() {
        return context;
    }

    public void setContext(AwardLimitContext context) {
        this.context = context;
    }
}
