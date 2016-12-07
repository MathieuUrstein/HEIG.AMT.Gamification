package ch.heigvd.gamification.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sebbos on 06.12.2016.
 */
@RestController
@RequestMapping("/badges")
class BadgeRestController {

    @RequestMapping(method = RequestMethod.GET)
    String read() {
        return "";
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<String> addBadge()
}
