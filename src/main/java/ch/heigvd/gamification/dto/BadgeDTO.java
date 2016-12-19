package ch.heigvd.gamification.dto;

public class BadgeDTO {
    private String name;
    private byte[] image;

    public BadgeDTO() {
    }

    public BadgeDTO(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }
}
