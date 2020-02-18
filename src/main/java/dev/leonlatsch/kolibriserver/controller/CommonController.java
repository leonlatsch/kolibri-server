package dev.leonlatsch.kolibriserver.controller;

import dev.leonlatsch.kolibriserver.constants.CommonResponses;
import dev.leonlatsch.kolibriserver.model.dto.Container;
import dev.leonlatsch.kolibriserver.util.MavenProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller for common functions
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class CommonController extends BaseController {

    /**
     * Endpoint for a healthcheck
     *
     * @return An empty {@link Container}
     */
    @RequestMapping(value = "healthcheck", method = RequestMethod.GET)
    public ResponseEntity<Container> healthCheck() {
        return createResponseEntity(CommonResponses.RES_OK);
    }

    /**
     * Endpoint to get the version number of the running instance
     *
     * @return The plain version number
     */
    @RequestMapping(value = "version", method = RequestMethod.GET)
    public String version() {
        return MavenProperties.getVersion();
    }
}
