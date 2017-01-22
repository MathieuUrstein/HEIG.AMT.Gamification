package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "An user generating various events in an application.")
public class UserDTO {
    @ApiModelProperty(value = "The username of the user.", required = true)
    private String username;

    @ApiModelProperty(value = "The badges awarded to the user.", required = true)
    private List<BadgeDTO> badges;

    @ApiModelProperty(value = "The points received by the user.", required = true)
    private List<PointsOnPointScaleDTO> points;

    public UserDTO() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public List<BadgeDTO> getBadges() {
        return badges;
    }

    public void setBadges(List<BadgeDTO> badges) {
        this.badges = badges;
    }

    public List<PointsOnPointScaleDTO> getPoints() {
        return points;
    }

    public void setPoints(List<PointsOnPointScaleDTO> points) {
        this.points = points;
    }
}
