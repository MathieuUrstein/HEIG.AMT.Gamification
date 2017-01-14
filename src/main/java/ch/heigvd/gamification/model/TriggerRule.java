package ch.heigvd.gamification.model;

import ch.heigvd.gamification.util.AwardLimitContext;

import javax.persistence.*;

@Entity
@Table(name="trigger_rule", uniqueConstraints = @UniqueConstraint(columnNames =  {"name", "application_id"}))
public class TriggerRule extends Rule {
    // TODO annotations
    private Badge awards;
    private int limit;
    private AwardLimitContext context = AwardLimitContext.ABOVE;

    public TriggerRule() {}

    public Badge getAwards() {
        return awards;
    }

    public void setAwards(Badge awards) {
        this.awards = awards;
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
