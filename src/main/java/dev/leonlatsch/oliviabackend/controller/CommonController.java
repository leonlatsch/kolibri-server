package dev.leonlatsch.oliviabackend.controller;

import dev.leonlatsch.oliviabackend.constants.CommonResponses;
import dev.leonlatsch.oliviabackend.dto.Container;
import dev.leonlatsch.oliviabackend.util.MavenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static dev.leonlatsch.oliviabackend.util.ControllerUtils.createResponseEntity;

/**
 * Rest Controller for common functions
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class CommonController {

    @Autowired
    private Environment env;

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
     * Endpoint just used for an overview.
     *
     * @return A String with the artifact id, the version and a hint to Github.
     */
    @RequestMapping
    public String overview() {
        String artifact = MavenProperties.getArtifactId();
        String version = MavenProperties.getVersion();
        return "Running " + artifact + " Version " + version + " \n\n" +
                "See the GitHub Wiki for Documentation";
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

    /**
     * Endpoint to get the configured broker port
     *
     * @return The plain broker port
     */
    @RequestMapping(value = "broker-port", method = RequestMethod.GET)
    public String brokerPort() {
        return env.getProperty("spring.rabbitmq.port");
    }
}
