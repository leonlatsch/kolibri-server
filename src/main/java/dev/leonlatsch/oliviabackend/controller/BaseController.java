package dev.leonlatsch.oliviabackend.controller;

import dev.leonlatsch.oliviabackend.constants.CommonResponses;
import dev.leonlatsch.oliviabackend.dto.Response;
import dev.leonlatsch.oliviabackend.util.MavenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static dev.leonlatsch.oliviabackend.util.ControllerUtils.createResponseEntity;

@RestController
@RequestMapping("/")
public class BaseController {

    @Autowired
    private Environment env;

    @RequestMapping(value = "healthcheck", method = RequestMethod.GET)
    public ResponseEntity<Response> healthCheck() {
        return createResponseEntity(CommonResponses.RES_OK);
    }

    @RequestMapping
    public String overview() {
        String group = MavenProperties.getGroupId();
        String artifact = MavenProperties.getArtifactId();
        String version = MavenProperties.getVersion();
        return "Running " + artifact + " Version " + version + " \n\n" +
                "See the GitHub Wiki for Documentation";
    }

    @RequestMapping(value = "version", method = RequestMethod.GET)
    public String version() {
        return MavenProperties.getVersion();
    }

    @RequestMapping(value = "broker-port", method = RequestMethod.GET)
    public String brokerPort() {
        return env.getProperty("spring.rabbitmq.port");
    }
}
