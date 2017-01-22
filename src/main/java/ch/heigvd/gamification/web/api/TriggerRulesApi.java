package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.TriggerRuleDTO;
import ch.heigvd.gamification.model.Application;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(value = "trigger rules", description = "the trigger rules API")
public interface TriggerRulesApi {

    @ApiOperation(
            value = "Retrieves all trigger rules for the current application.",
            notes = "",
            response = TriggerRuleDTO.class,
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
                    response = TriggerRuleDTO.class
            )
    })
    List<TriggerRuleDTO> getTriggerRules(Application app);

    @ApiOperation(
            value = "Retrieves a given trigger rule.",
            notes = "",
            response = TriggerRuleDTO.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = TriggerRuleDTO.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Trigger rule not found.",
                    response = TriggerRuleDTO.class)
    })
    TriggerRuleDTO getTriggerRule(Application app,
                                  @ApiParam(value = "The name of the trigger rule.", required = true)
                                  @PathVariable("name") String name);

    @ApiOperation(
            value = "Creates a new trigger rule.",
            notes = "",
            response = Void.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {}
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
                    message = "Error code 10: Specified point scale doesn't exist. \n\r Error code 11: Badge doesn't exist.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "Error code 3: Trigger rule name must be unique in the current application.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> createTriggerRule(Application app,
                                           @ApiParam(value = "The info needed to create a new trigger rule.",
                                                   required = true)
                                           @RequestBody TriggerRuleDTO body);

    @ApiOperation(
            value = "Deletes a given trigger rule.",
            notes = "", response = Void.class,
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
                    message = "Trigger rule not found.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> deleteTriggerRule(Application app,
                                           @ApiParam(value = "The name of the given trigger rule.", required = true)
                                           @PathVariable("name") String name);

    @ApiOperation(value = "Makes a complete update for a given trigger rule.",
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
                    message = "Error code 10: Specified point scale doesn't exist. \n\r Error code 11: Badge doesn't exist.",
                    response = Void.class
            ),
            @ApiResponse(code = 404,
                    message = "Trigger rule not found.",
                    response = Void.class
            ),
            @ApiResponse(code = 409,
                    message = "Error code 3: Trigger rule name must be unique in the current application.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> completeUpdateTriggerRule(@ApiIgnore @RequestAttribute("application") Application application,
                                                   @ApiParam(value = "The name of the trigger rule.", required = true) @PathVariable("name") String name,
                                                   @ApiParam(value = "The new info of the trigger rule.", required=true) @Valid @RequestBody TriggerRuleDTO ruleDTO);

}
