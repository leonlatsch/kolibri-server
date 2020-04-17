package dev.leonlatsch.kolibriserver.controller;

import dev.leonlatsch.kolibriserver.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
