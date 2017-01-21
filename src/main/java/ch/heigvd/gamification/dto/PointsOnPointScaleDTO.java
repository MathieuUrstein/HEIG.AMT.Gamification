package ch.heigvd.gamification.dto;


public class PointsOnPointScaleDTO {
    private long pointScaleId;
    private int points;

    public PointsOnPointScaleDTO() {
    }

    public long getPointScaleId() {
        return pointScaleId;
    }

    public void setPointScaleId(long pointScaleId) {
        this.pointScaleId = pointScaleId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
