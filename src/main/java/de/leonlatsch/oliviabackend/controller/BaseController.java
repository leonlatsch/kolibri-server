package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.constants.CommonResponses;
import de.leonlatsch.oliviabackend.dto.Response;
import de.leonlatsch.oliviabackend.util.MavenProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static de.leonlatsch.oliviabackend.util.ControllerUtils.createResponseEntity;

@RestController
@RequestMapping("/")
public class BaseController {

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
}
