package dev.leonlatsch.kolibriserver.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Static class to get values from pom.xml
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class MavenProperties {

    private static final Logger log = LoggerFactory.getLogger(MavenProperties.class);

    private static String artifactId;
    private static String groupId;
    private static String version;

    private static void load() {
        if (artifactId == null || groupId == null || version == null) {
            try {
                MavenXpp3Reader reader = new MavenXpp3Reader();
                Model model;
                if ((new File("pom.xml")).exists())
                    model = reader.read(new FileReader("pom.xml"));
                else
                    model = reader.read(MavenProperties.class.getResourceAsStream("/META-INF/maven/dev.leonlatsch/kolibri-server/pom.xml"));
                artifactId = model.getArtifactId();
                groupId = model.getGroupId();
                version = model.getVersion();
            } catch (IOException | XmlPullParserException e) {
                log.error("Error loading maven properties from pom.xml: " + e);
            }
        }
    }

    public static String getArtifactId() {
        load();
        return artifactId;
    }

    public static String getGroupId() {
        load();
        return groupId;
    }

    public static String getVersion() {
        load();
        return version;
    }
}
