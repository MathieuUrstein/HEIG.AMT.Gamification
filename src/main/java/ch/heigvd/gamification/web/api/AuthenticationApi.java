package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.CredentialsDTO;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Api(value = "auth", description = "the auth API")
public interface AuthenticationApi {
    @ApiOperation(
            value = "Logs an application in.",
            response = Void.class,
            tags = {}
    )
    // TODO error codes
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Error code todo: Application already authenticated.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 401,
                    message =
                            "- Error code 4: The application does not exist.\n" +
                                    "- Error code 5: Authentication failed.\n" +
                                    "- Error code todo: JWT invalid.\n" +
                                    "- Error code todo: Invalid JWT format.",
                    response = Void.class
            )
    })
    @RequestMapping(
            value = "/auth/",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST
    )
    ResponseEntity login(@ApiParam(value = "The credentials of the application.", required = true)
                         @Valid @RequestBody CredentialsDTO body);

}
