package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "How many points an user has on a given point scale.")
public class PointsOnPointScaleDTO {
    @ApiModelProperty(value = "The point scale.", required = true)
    private String pointScale;

    @ApiModelProperty(value = "How many points are on the point scale.", required = true)
    private int points;

    public PointsOnPointScaleDTO() {
    }

    public PointsOnPointScaleDTO(String pointScale, int points) {
        this.pointScale = pointScale;
        this.points = points;
    }

    public String getPointScaleId() {
        return pointScale;
    }

    public void setPointScaleId(String pointScale) {
        this.pointScale = pointScale;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
