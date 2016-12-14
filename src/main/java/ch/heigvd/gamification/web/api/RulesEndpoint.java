package ch.heigvd.gamification.web.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sebbos on 07.12.2016.
 */
@RestController
@RequestMapping("/rules")
public class RulesEndpoint {

    @RequestMapping(method = RequestMethod.GET)
    String read() {
        return "";
    }
}
