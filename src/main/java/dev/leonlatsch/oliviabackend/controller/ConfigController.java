package dev.leonlatsch.oliviabackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for accessing configuration.
 *
 * @author Leon Latscj
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/config")
public class ConfigController {

    @Autowired
    private Environment env;

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
