package ch.heigvd.gamification.web.api;


import java.math.BigDecimal;

import ch.heigvd.gamification.dto.BadgeDTO;
import ch.heigvd.gamification.model.Application;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(
            value = "/badges/",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET
    )
    List<BadgeDTO> getBadges(@RequestAttribute("application") Application app);

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
            )
    })
    @RequestMapping(
            value = "/badges/{id}/",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET
    )
    BadgeDTO getBadge(@RequestAttribute("application") Application app,
                      @ApiParam(value = "The id of the badge.", required = true) @PathVariable("id") long id);

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
                    response = Void.class
            )
    })
    @RequestMapping(value = "/badges/",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity createBadge(@RequestAttribute("application") Application application,
                               @ApiParam(value = "The info needed to create the badge.", required = true)
                               @Valid @RequestBody BadgeDTO badgeDTO);

    /*
    @ApiOperation(value = "Partially updates a given badge.", notes = "", response = Void.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation.", response = Void.class)})
    @RequestMapping(value = "/badges/{id}/",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PATCH)
    ResponseEntity<Void> badgesIdPatch(@ApiParam(value = "The id of the badge.", required = true) @PathVariable("id")
                                               BigDecimal id,
                                       @ApiParam(value = "The new info of the badge.") @RequestBody Badge body);


    @ApiOperation(value = "Updates a given badge.", notes = "", response = Void.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation.", response = Void.class)})
    @RequestMapping(value = "/badges/{id}/",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<Void> badgesIdPut(@ApiParam(value = "The id of the badge.", required = true) @PathVariable("id")
                                             BigDecimal id,
                                     @ApiParam(value = "The new info of the badge.") @RequestBody Badge body);*/

    @ApiOperation(value = "Deletes a given badge.", notes = "", response = Void.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = Void.class
            )
    })
    @RequestMapping(
            value = "/badges/{id}/",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.DELETE
    )
    ResponseEntity deleteBadge(@RequestAttribute("application") Application app,
                               @ApiParam(value = "The id of the badge.", required = true)
                               @PathVariable("id") long id);

}
