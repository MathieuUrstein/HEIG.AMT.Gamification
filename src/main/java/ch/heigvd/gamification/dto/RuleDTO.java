package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModelProperty;

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

    @ApiModelProperty(value = "The name of the rule.", required = true)
    public String getName() {
        return name;
    }
}
