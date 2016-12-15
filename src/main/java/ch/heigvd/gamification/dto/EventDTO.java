package ch.heigvd.gamification.dto;

/**
 * Created by sebbos on 14.12.2016.
 */
public class EventDTO {
    private String type;
    private String username;

    public EventDTO() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }
}
