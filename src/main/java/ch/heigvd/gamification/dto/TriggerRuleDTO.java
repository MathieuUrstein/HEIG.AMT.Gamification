package ch.heigvd.gamification.dto;

public class TriggerRuleDTO extends RuleDTO {
    private String badgeAwarded;
    private String pointScale;
    private int limit;
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
