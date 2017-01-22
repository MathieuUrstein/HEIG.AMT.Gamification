package ch.heigvd.gamification.web.api;


import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.model.Application;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(value = "badges", description = "the badges API")
public interface BadgesApi {

    @ApiOperation(
            value = "Retrieves all badges for the current application.",
            notes = "",
            response = BadgeDTO.class,
            responseContainer = "List", authorizations = {
            @Authorization(value = "JWT")
    },
            tags = {})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = BadgeDTO.class
            )
    })
    List<BadgeDTO> getBadges(@ApiIgnore @RequestAttribute("application") Application app);

    @ApiOperation(
            value = "Retrieves a given badge.",
            notes = "",
            response = BadgeDTO.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = BadgeDTO.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Badge not found.",
                    response = Void.class
            )
    })
    BadgeDTO getBadge(@ApiIgnore @RequestAttribute("application") Application app,
                      @ApiParam(value = "The name of the badge.", required = true) @PathVariable("name") String name);

    @ApiOperation(
            value = "Creates a new badge.",
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
                    code = 409,
                    message = "Error code 3: Badge name must be unique in the current application.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> createBadge(@ApiIgnore @RequestAttribute("application") Application application,
                                     @ApiParam(value = "The info needed to create the badge.", required = true)
                                     @Valid @RequestBody BadgeDTO badgeDTO);

    @ApiOperation(value = "Makes a complete update for a given badge.",
            notes = "",
            response = Void.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Badge not found.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "Error code 3: Badge name must be unique in the current application.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> completeUpdateBadge(@ApiIgnore @RequestAttribute("application") Application application,
                                             @ApiParam(value = "The name of the badge.", required = true) @PathVariable("name") String name,
                                             @ApiParam(value = "The new info of the badge.", required=true) @Valid @RequestBody BadgeDTO badgeDTO);

    @ApiOperation(value = "Deletes a given badge.", notes = "", response = Void.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Badge not found.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> deleteBadge(@ApiIgnore @RequestAttribute("application") Application app,
                                     @ApiParam(value = "The name of the badge.", required = true)
                                     @PathVariable("name") String name);

}
