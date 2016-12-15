package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.PointScaleRepository;
import ch.heigvd.gamification.dto.PointScaleDTO;
import ch.heigvd.gamification.exception.ConflictException;
import ch.heigvd.gamification.exception.NotFoundException;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.model.PointScale;
import ch.heigvd.gamification.util.URIs;
import ch.heigvd.gamification.validator.FieldsRequiredAndNotEmptyValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping(URIs.POINT_SCALES)
public class PointScalesEndpoint {
    private final PointScaleRepository pointScaleRepository;

    public PointScalesEndpoint(PointScaleRepository pointScaleRepository) {
        this.pointScaleRepository = pointScaleRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(PointScaleDTO.class));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<PointScale> getPointScales(@RequestAttribute("application") Application app) {
        return pointScaleRepository.findByApplicationName(app.getName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{pointScaleId}")
    public PointScale getPointScale(@PathVariable Long pointScaleId, @RequestAttribute("application") Application app) {
        return pointScaleRepository
                .findByApplicationNameAndId(app.getName(), pointScaleId)
                .orElseThrow(() -> new NotFoundException("pointScale", pointScaleId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addBadge(@Valid @RequestBody PointScaleDTO badgeDTO,
                                   @RequestAttribute("application") Application app) {
        try {
            PointScale pointScale = new PointScale();
            pointScale.setName(badgeDTO.getName());
            pointScale.setApplication(app);

            pointScaleRepository.save(pointScale);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(pointScale.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("pointScale", badgeDTO.getName());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{pointScaleId}")
    public ResponseEntity deleteBadge(@PathVariable Long pointScaleId, @RequestAttribute("application") Application app) {
        PointScale pointScale = pointScaleRepository
                .findByApplicationNameAndId(app.getName(), pointScaleId)
                .orElseThrow(() -> new NotFoundException("pointScale", pointScaleId));

        pointScaleRepository.delete(pointScale);

        return ResponseEntity.ok().build();
    }
}
