package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dto.EventDTO;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.services.EventProcessor;
import ch.heigvd.gamification.util.URIs;
import ch.heigvd.gamification.validator.FieldsRequiredAndNotEmptyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping(URIs.EVENTS)
public class EventsEndpoint implements EventsApi {
    private final EventProcessor eventProcessor;

    @Autowired
    public EventsEndpoint(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(EventDTO.class));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createEvent(@ApiIgnore @RequestAttribute("application") Application app,
                                            @Valid @RequestBody EventDTO eventDTO) {

        // retry processing event as long as there is a
        boolean exception;
        do {
            exception = false;
            try {
                eventProcessor.processEvent(app, eventDTO);
            } catch (DataIntegrityViolationException e) {
                exception = true;
            }
        } while (exception);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
