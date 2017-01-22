package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.EventRuleDTO;
import ch.heigvd.gamification.model.Application;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(value = "event rules", description = "the event rules API")
public interface EventRulesAPi {

    @ApiOperation(
            value = "Retrieves all event rules for the current application.",
            notes = "",
            response = EventRuleDTO.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "JWT")
            },
            tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = EventRuleDTO.class
            )
    })
    List<EventRuleDTO> getEventRules(Application app);

    @ApiOperation(
            value = "Retrieves a given event rule.",
            notes = "",
            response = EventRuleDTO.class,
            authorizations = {
                    @Authorization(value = "JWT")
            },
            tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = EventRuleDTO.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Event rule not found.",
                    response = EventRuleDTO.class
            )
    })
    EventRuleDTO getEventRule(Application app,
                              @ApiParam(value = "The name of the event rule.", required = true)
                              @PathVariable("name") String name);

    @ApiOperation(
            value = "Creates a new event rule.",
            notes = "",
            response = Void.class,
            authorizations = {
                    @Authorization(value = "JWT")
            },
            tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Successful operation.",
                    response = Void.class,
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "Location",
                                    description = "URI of newly created object.",
                                    response = String.class
                            )
                    }
            ),
            @ApiResponse(
                    code = 400,
                    message = "Error code 10: Specified point scale doesn't exist.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "Error code 3: Event rule name must be unique in the current " +
                            "application.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> createEventRule(Application app,
                                         @ApiParam(value = "The info needed to create a new event rule.",
                                                 required = true)
                                         @RequestBody EventRuleDTO body);

    @ApiOperation(value = "Makes a complete update for a given event rule.",
            notes = "",
            response = Void.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags={})
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Error code 10: Specified point scale doesn't exist.",
                    response = Void.class
            ),
            @ApiResponse(code = 404,
                    message = "Event rule not found.",
                    response = Void.class
            ),
            @ApiResponse(code = 409,
                    message = "Error code 3: Event rule name must be unique in the current application.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> completeUpdateEventRule(@ApiIgnore @RequestAttribute("application") Application application,
                                                 @ApiParam(value = "The name of the event rule.", required = true) @PathVariable("name") String name,
                                                 @ApiParam(value = "The new info of the event rule.", required=true) @Valid @RequestBody EventRuleDTO ruleDTO);

    @ApiOperation(
            value = "Deletes the given event rule.",
            notes = "",
            response = Void.class,
            authorizations = {
                    @Authorization(value = "JWT")
            },
            tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Event rule not found.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> deleteEventRule(Application app,
                                         @ApiParam(value = "The name of the given event rule.", required = true)
                                         @PathVariable("name") String name);

}
