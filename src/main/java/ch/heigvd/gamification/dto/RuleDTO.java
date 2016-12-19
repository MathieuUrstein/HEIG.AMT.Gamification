package ch.heigvd.gamification.dto;

public class RuleDTO {
    private String name;

    public RuleDTO() {
    }

    public RuleDTO(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
