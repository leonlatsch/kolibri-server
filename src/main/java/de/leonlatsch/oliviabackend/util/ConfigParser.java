package de.leonlatsch.oliviabackend.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigParser {

    private static final String SECURITY_PROPERTIES = "security.properties";

    public static Properties loadSecurityProperties() throws IOException {
        InputStream inputStream = ConfigParser.class.getClassLoader().getResourceAsStream(SECURITY_PROPERTIES);
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
