package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.PointScaleDTO;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.PointScale;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "pointScales", description = "the pointScales API")
public interface PointScalesApi {

    @ApiOperation(
            value = "Retrieves all the point scales of the current application.",
            notes = "",
            response = PointScale.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = PointScale.class
            )
    })
    List<PointScaleDTO> getPointScales(Application app);

    @ApiOperation(
            value = "Retrieves the given point scale.",
            notes = "",
            response = PointScaleDTO.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = PointScaleDTO.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Point scale not found.",
                    response = Void.class
            )
    })
    PointScaleDTO getPointScale(Application app,
                                @ApiParam(value = "The name of the point scale.", required = true)
                                @PathVariable("name") String name);


    @ApiOperation(
            value = "Creates a point scale.",
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
                    message = "Error code 3: Point scale name must be unique in the current application.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> createPointScale(Application app,
                                          @ApiParam(value = "The info needed to create a point scale.", required = true)
                                          @RequestBody PointScaleDTO pointScaleDTO);


    // TODO
    /*
    @ApiOperation(value = "Partially updates the given point scale.", notes = "", response = Void.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation.", response = Void.class),
            @ApiResponse(code = 404, message = "Point scale not found.", response = Void.class),
            @ApiResponse(code = 409, message = "Error code 3: Point scale name must be unique in the current " +
                    "application.", response = Void.class)})
    ResponseEntity<Void> pointScalesIdPatch(@ApiParam(value = "The id of the point scale.", required = true)
                                            @PathVariable("id") BigDecimal id,
                                            @ApiParam(value = "The new info of the point scale.") @RequestBody
                                                    PointScale body);



    @ApiOperation(value = "Updates the given point scale.", notes = "", response = Void.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation.", response = Void.class),
            @ApiResponse(code = 404, message = "Point scale not found.", response = Void.class),
            @ApiResponse(code = 409, message = "Error code 3: Point scale name must be unique in the current " +
                    "application.", response = Void.class)})
    ResponseEntity<Void> pointScalesIdPut(@ApiParam(value = "The id of the point scale.", required = true)
                                          @PathVariable("id") BigDecimal id,
                                          @ApiParam(value = "The new info of the point scale.", required = true)
                                          @RequestBody PointScale body);
    */

    @ApiOperation(
            value = "Deletes the given point scale.",
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
                    message = "Point scale not found.",
                    response = Void.class
            )
    })
    ResponseEntity<Void> deletePointScale(Application app,
                                          @ApiParam(value = "The name of the point scale.", required = true)
                                          @PathVariable("name") String name);
}
