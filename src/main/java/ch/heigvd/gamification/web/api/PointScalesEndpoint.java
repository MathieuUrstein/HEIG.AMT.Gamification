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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(URIs.POINT_SCALES)
public class PointScalesEndpoint implements PointScalesApi {
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
    public List<PointScaleDTO> getPointScales(@ApiIgnore @RequestAttribute("application") Application app) {
        return pointScaleRepository.findByApplicationName(app.getName())
                .stream()
                .map(this::toPointScaleDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{name}")
    public PointScaleDTO getPointScale(@ApiIgnore @RequestAttribute("application") Application app,
                                       @PathVariable String name) {
        PointScale pointScale = pointScaleRepository
                .findByApplicationNameAndName(app.getName(), name)
                .orElseThrow(NotFoundException::new);

        return toPointScaleDTO(pointScale);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createPointScale(@ApiIgnore @RequestAttribute("application") Application application,
                                                 @Valid @RequestBody PointScaleDTO pointScaleDTO) {
        try {
            Application app = applicationRepository.findByName(application.getName());
            PointScale pointScale = new PointScale();

            pointScale.setName(pointScaleDTO.getName());
            pointScale.setApplication(app);
            app.addPointScale(pointScale);

            pointScaleRepository.save(pointScale);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{name}/")
                    .buildAndExpand(pointScale.getName()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DataIntegrityViolationException e) {
            // The name of a point scales must be unique in a gamified application
            throw new ConflictException("name");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
    public ResponseEntity<Void> deletePointScale(@ApiIgnore @RequestAttribute("application") Application app,
                                                 @PathVariable String name) {
        PointScale pointScale = pointScaleRepository
                .findByApplicationNameAndName(app.getName(), name)
                .orElseThrow(NotFoundException::new);

        pointScaleRepository.delete(pointScale);

        return ResponseEntity.ok().build();
    }

    private PointScaleDTO toPointScaleDTO(PointScale pointScale) {
        return new PointScaleDTO(pointScale.getName());
    }

}
