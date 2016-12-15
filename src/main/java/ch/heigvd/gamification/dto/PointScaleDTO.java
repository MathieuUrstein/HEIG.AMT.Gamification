package ch.heigvd.gamification.dto;

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

    public String getName() {
        return name;
    }

}
