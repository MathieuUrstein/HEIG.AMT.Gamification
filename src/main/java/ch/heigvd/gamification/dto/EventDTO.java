package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "An event created when an user does something on the application.")
public class EventDTO {
    @ApiModelProperty(value = "The type of the event.", required = true)
    private String type;

    @ApiModelProperty(value = "The username of the user.", required = true)
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
