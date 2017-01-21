package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.CredentialsDTO;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(value = "register", description = "the register API")
public interface RegisterApi {

    @ApiOperation(
            value = "Registers a gamified application.",
            notes = "",
            response = Void.class,
            tags={}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Successful operation.",
                    response = Void.class,
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "Authorization",
                                    description = "The JTW token.",
                                    response = String.class
                            )
                    }
            ),
            @ApiResponse(
                    code = 409,
                    message = "Error code 3: Application name must be unique.",
                    response = Void.class
            )
    })
    @RequestMapping(
            value = "/register/",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST
    )
    ResponseEntity<Void> register(@ApiParam(value = "The credentials of the application to create.", required = true)
                                  @RequestBody CredentialsDTO body);

}
