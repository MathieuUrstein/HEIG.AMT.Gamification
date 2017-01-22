package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A point scale in an application.")
public class PointScaleDTO {
    private String name;

    public PointScaleDTO() {
    }

    public PointScaleDTO(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "The name of the point scale.", required = true)
    public String getName() {
        return name;
    }

}
