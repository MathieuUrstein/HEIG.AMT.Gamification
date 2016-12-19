package ch.heigvd.gamification.web.api;

import ch.heigvd.gamification.dao.ApplicationRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(URIs.POINT_SCALES)
public class PointScalesEndpoint {
    private final PointScaleRepository pointScaleRepository;
    private final ApplicationRepository applicationRepository;

    public PointScalesEndpoint(PointScaleRepository pointScaleRepository, ApplicationRepository applicationRepository) {
        this.pointScaleRepository = pointScaleRepository;
        this.applicationRepository = applicationRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FieldsRequiredAndNotEmptyValidator(PointScaleDTO.class));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PointScaleDTO> getPointScales(@RequestAttribute("application") Application app) {
        return pointScaleRepository.findByApplicationName(app.getName())
                .stream()
                .map(this::toPointScaleDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{pointScaleId}")
    public PointScaleDTO getPointScale(@RequestAttribute("application") Application app, @PathVariable long pointScaleId) {
        PointScale pointScale = pointScaleRepository
                .findByApplicationNameAndId(app.getName(), pointScaleId)
                .orElseThrow(() -> new NotFoundException("pointScale", pointScaleId));

        return toPointScaleDTO(pointScale);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addPointScale(@Valid @RequestBody PointScaleDTO badgeDTO,
                                   @RequestAttribute("application") Application application) {
        try {
            Application app = applicationRepository.findByName(application.getName());
            PointScale pointScale = new PointScale();

            pointScale.setName(badgeDTO.getName());
            pointScale.setApplication(app);
            app.addPointScale(pointScale);

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
    public ResponseEntity deletePointScale(@RequestAttribute("application") Application app, @PathVariable long pointScaleId) {
        PointScale pointScale = pointScaleRepository
                .findByApplicationNameAndId(app.getName(), pointScaleId)
                .orElseThrow(() -> new NotFoundException("pointScale", pointScaleId));

        pointScaleRepository.delete(pointScale);

        return ResponseEntity.ok().build();
    }

    private PointScaleDTO toPointScaleDTO(PointScale pointScale) {
        return new PointScaleDTO(pointScale.getName());
    }

}
