package ch.heigvd.gamification.dto;

/**
 * Created by sebbos on 14.12.2016.
 */
public class EventDTO {
    private String type;
    private String userName;

    public EventDTO() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public String getUserName() {
        return userName;
    }
}
