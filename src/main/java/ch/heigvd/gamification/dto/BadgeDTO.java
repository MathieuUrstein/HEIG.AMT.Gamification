package ch.heigvd.gamification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A badge in an application.
 */
@ApiModel(description = "A badge in an application.")
public class BadgeDTO {
    private String name;

    private byte[] image;

    public BadgeDTO() {
    }

    public BadgeDTO(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    @ApiModelProperty(value = "The name of the badge.", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @ApiModelProperty(value = "The image of the badge.")
    public byte[] getImage() {
        return image;
    }
}
