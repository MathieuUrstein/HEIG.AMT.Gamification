package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.EventRuleDTO;
import ch.heigvd.gamification.model.Application;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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

    // TODO
    /*
    @ApiOperation(value = "Partially updates the given event rule.", notes = "", response = Void.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation.", response = Void.class),
            @ApiResponse(code = 404, message = "Event rule not found.", response = Void.class),
            @ApiResponse(code = 409, message = "Error code 3: Event rule name must be unique in the current " +
                    "application.", response = Void.class)})
    ResponseEntity<Void> rulesEventsIdPatch(@ApiParam(value = "The id of the given event rule.", required = true)
                                            @PathVariable("id") BigDecimal id,
                                            @ApiParam(value = "The modified fields of the event rule.") @RequestBody
                                                    EventRuleDTO body);


    @ApiOperation(value = "Updates the given event rule.", notes = "", response = Void.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation.", response = Void.class),
            @ApiResponse(code = 404, message = "Event rule not found.", response = Void.class),
            @ApiResponse(code = 409, message = "Error code 3: Event rule name must be unique in the current " +
                    "application.", response = Void.class)})
    ResponseEntity<Void> rulesEventsIdPut(@ApiParam(value = "The id of the given event rule.", required = true)
                                          @PathVariable("id") BigDecimal id,
                                          @ApiParam(value = "The modified event rule.", required = true) @RequestBody
                                                  EventRuleDTO body);

    */

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
