package ch.heigvd.gamification.model;

import javax.persistence.*;

@Entity
@Table(name = "rule", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "application_id"}))
@Inheritance(strategy = InheritanceType.JOINED)
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    public Rule() {}

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
