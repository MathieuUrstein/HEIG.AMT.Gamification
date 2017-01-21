package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.UserDTO;
import ch.heigvd.gamification.model.Application;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "users", description = "the users API")
public interface UsersApi {

    @ApiOperation(
            value = "Retrieves all the users of the current application.",
            notes = "Get all the users of the current application with their badges and points awarded.",
            response = UserDTO.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags={}
    )
    @ApiResponses(value = { 
        @ApiResponse(
                code = 200,
                message = "Successful operation.",
                response = UserDTO.class
        )
    })
    @RequestMapping(
            value = "/users/",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    List<UserDTO> getUsers(Application app);


    @ApiOperation(
            value = "Retrieves a specified user.",
            notes = "",
            response = UserDTO.class,
            authorizations = {
                    @Authorization(value = "JWT")
            }, tags={}
    )
    @ApiResponses(value = { 
        @ApiResponse(
                code = 200,
                message = "Successful operation",
                response = UserDTO.class
        ),
        @ApiResponse(
                code = 404,
                message = "User not found.",
                response = UserDTO.class
        )
    })
    @RequestMapping(
            value = "/users/{username}/",
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    UserDTO getUser(Application app,
                    @ApiParam(value = "The username of the user.", required = true)
                    @PathVariable("username") String username);

}
