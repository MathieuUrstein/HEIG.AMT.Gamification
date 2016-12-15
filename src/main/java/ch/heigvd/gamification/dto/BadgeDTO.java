package ch.heigvd.gamification.dto;

/**
 * Created by sebbos on 12.12.2016.
 */
public class BadgeDTO {
    private String name;
    private byte[] image;

    public BadgeDTO() {
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
