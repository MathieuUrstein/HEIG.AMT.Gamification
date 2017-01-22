package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.model.Application;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(value = "events", description = "the events API")
public interface EventsApi {

    @ApiOperation(
            value = "Creates an event.",
            notes = "Creates an event triggered by an action made by an user of the application. " +
                    "If the user does not exist in the platform, he will be created.",
            response = Void.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Successful operation.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> createEvent(@ApiIgnore @RequestAttribute("application") Application app,
                                     @ApiParam(value = "The info of the event.", required = true)
                                     @Valid @RequestBody EventDTO eventDTO);

}
