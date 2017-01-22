package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "An event created when an user does something on the application.")
public class EventDTO {
    private String type;
    private String username;

    public EventDTO() {
    }

    public void setType(String type) {
        this.type = type;
    }

    @ApiModelProperty(value = "The type of the event.", required = true)
    public String getType() {
        return type;
    }

    @ApiModelProperty(value = "The username of the user.", required = true)
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
