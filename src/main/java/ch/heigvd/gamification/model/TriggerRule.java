package ch.heigvd.gamification.model;

import ch.heigvd.gamification.util.AwardLimitContext;

import javax.persistence.*;

@Entity
@Table(name="trigger_rule", uniqueConstraints = @UniqueConstraint(columnNames =  {"name", "application_id"}))
public class TriggerRule extends Rule {
    // TODO annotations
    private Badge badgeAwarded;
    private PointScale pointScale;
    private int limit;
    private AwardLimitContext context = AwardLimitContext.ABOVE;

    public TriggerRule() {}

    public Badge getBadgeAwarded() {
        return badgeAwarded;
    }

    public PointScale getPointScale() {
        return pointScale;
    }

    public void setPointScale(PointScale pointScale) {
        this.pointScale = pointScale;
    }

    public void setBadgeAwarded(Badge badgeAwarded) {
        this.badgeAwarded = badgeAwarded;
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
