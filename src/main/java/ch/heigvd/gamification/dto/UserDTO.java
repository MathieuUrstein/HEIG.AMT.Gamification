package ch.heigvd.gamification.dto;

/**
 * Created by sebbos on 19.12.2016.
 */
public class UserDTO {
    // TODO : send links

    private String name;

    public UserDTO(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
