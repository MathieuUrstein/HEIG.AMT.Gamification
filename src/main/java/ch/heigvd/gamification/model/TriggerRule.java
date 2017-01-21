package ch.heigvd.gamification.model;

import javax.persistence.*;

@Entity
@Table(name = "trigger_rule")
@PrimaryKeyJoinColumn(name = "id")
public class TriggerRule extends Rule {
    @Column(name = "[limit]", nullable = false)
    private int limit;

    @Column(name = "above_limit", nullable = false)
    private boolean aboveLimit = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_awarded_id", nullable = false)
    private Badge badgeAwarded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_scale_id", nullable = false)
    private PointScale pointScale;

    public TriggerRule() {
        super();
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
}
