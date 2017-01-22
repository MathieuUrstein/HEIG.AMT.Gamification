package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "How many points an user has on a given point scale.")
public class PointsOnPointScaleDTO {
    private String pointScale;
    private Integer points;

    public PointsOnPointScaleDTO() {
    }

    public PointsOnPointScaleDTO(String pointScale, Integer points) {
        this.pointScale = pointScale;
        this.points = points;
    }

    @ApiModelProperty(value = "The point scale.", required = true)
    public String getPointScale() {
        return pointScale;
    }

    public void setPointScale(String pointScale) {
        this.pointScale = pointScale;
    }

    @ApiModelProperty(value = "How many points are on the point scale.", required = true)
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
