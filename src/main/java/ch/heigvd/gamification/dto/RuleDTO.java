package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModelProperty;

public class RuleDTO {
    @ApiModelProperty(value = "The name of the rule.", required = true)
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
