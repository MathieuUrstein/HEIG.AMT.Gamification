package ch.heigvd.gamification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "The credentials of an application.")
public class CredentialsDTO {
    @ApiModelProperty(value = "The username of the application.", required = true)
    private String name;

    @ApiModelProperty(value = "The password of the application.", required = true)
    private String password;

    public CredentialsDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
