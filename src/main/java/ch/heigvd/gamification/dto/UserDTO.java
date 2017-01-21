package ch.heigvd.gamification.dto;

import java.util.List;

public class UserDTO {
    private String username;
    private List<BadgeDTO> badges;
    private List<PointsOnPointScaleDTO> points;

    public UserDTO() {
    }

    public UserDTO(String username) {
        this.username = username;
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
