package dev.leonlatsch.kolibriserver.controller;

import dev.leonlatsch.kolibriserver.util.MavenProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller to display an overview at the main page.
 * WILL BE REPLACED WITH A DASHBOARD!
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@RequestMapping("/")
@RestController
public class OverviewController extends BaseController {

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
                "See the Wiki for Documentation";
    }
}
