package dev.leonlatsch.kolibriserver.controller;

import dev.leonlatsch.kolibriserver.constants.Headers;
import dev.leonlatsch.kolibriserver.model.dto.ConfigEntry;
import dev.leonlatsch.kolibriserver.model.dto.Container;
import dev.leonlatsch.kolibriserver.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller for accessing configuration.
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/config")
public class ConfigController extends BaseController {

    @Autowired
    private Environment env;

    @Autowired
    private ConfigService configService;

    /**
     * Endpoint to get the configured broker port
     *
     * @return The plain broker port
     */
    @RequestMapping(value = "broker-port", method = RequestMethod.GET)
    public String brokerPort() {
        return env.getProperty("spring.rabbitmq.port");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public ResponseEntity<Container> get() {
        return createResponseEntity(configService.get());
    }

    @RequestMapping(value = "put", method = RequestMethod.PUT)
    public ResponseEntity<Container> put(@RequestBody ConfigEntry configEntry, @RequestHeader(Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(configService.put(configEntry.getKey(), configEntry.getValue(), accessToken));
    }

    @RequestMapping(value = "reset/{key}", method = RequestMethod.PATCH)
    public ResponseEntity<Container> reset(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("key") String key) {
        return createResponseEntity(configService.reset(key, accessToken));
    }
}
