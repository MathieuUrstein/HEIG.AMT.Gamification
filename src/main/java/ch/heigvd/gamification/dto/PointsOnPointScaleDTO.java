package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "How many points an user has on a given point scale.")
public class PointsOnPointScaleDTO {
    private String pointScale;
    private int points;

    public PointsOnPointScaleDTO() {
    }

    public PointsOnPointScaleDTO(String pointScale, int points) {
        this.pointScale = pointScale;
        this.points = points;
    }

    @ApiModelProperty(value = "The point scale.", required = true)
    public String getPointScaleId() {
        return pointScale;
    }

    public void setPointScaleId(String pointScale) {
        this.pointScale = pointScale;
    }

    @ApiModelProperty(value = "How many points are on the point scale.", required = true)
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
