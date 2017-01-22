package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A badge in an application.
 */
@ApiModel(description = "A badge in an application.")
public class BadgeDTO {
    private String name;

    public BadgeDTO() {
    }

    public BadgeDTO(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "The name of the badge.", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
