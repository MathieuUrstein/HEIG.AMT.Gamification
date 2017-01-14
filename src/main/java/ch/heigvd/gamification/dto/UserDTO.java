package ch.heigvd.gamification.dto;

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
