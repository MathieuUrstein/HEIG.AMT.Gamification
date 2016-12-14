package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.util.URIs;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sebbos on 07.12.2016.
 */
@RestController
@RequestMapping(URIs.EVENTS)
public class EventsEndpoint {
    @RequestMapping(method = RequestMethod.GET)
    public String read() {
        return "";
    }
}
