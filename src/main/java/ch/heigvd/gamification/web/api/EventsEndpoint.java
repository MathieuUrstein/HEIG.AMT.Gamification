package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.services.EventProcessor;
import ch.heigvd.gamification.util.URIs;
import ch.heigvd.gamification.validator.EventDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

/**
 * Created by sebbos on 07.12.2016.
 */
@RestController
@RequestMapping(URIs.EVENTS)
public class EventsEndpoint {
    private final EventProcessor eventProcessor;

    @Autowired
    public EventsEndpoint(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new EventDTOValidator());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addEvent(@Valid @RequestBody EventDTO eventDTO, ServletRequest request) {
        Application app = (Application) request.getAttribute("application");

        try {
            eventProcessor.processEvent(app, eventDTO);
        }
        catch (DataIntegrityViolationException e) {
            // We relaunch the request when it fails
            eventProcessor.processEvent(app, eventDTO);
        }

        return ResponseEntity.ok().build();
    }
}
