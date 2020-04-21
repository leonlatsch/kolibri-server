package dev.leonlatsch.kolibriserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.leonlatsch.kolibriserver.constants.Configs;
import dev.leonlatsch.kolibriserver.constants.FormatsAndFiles;
import dev.leonlatsch.kolibriserver.model.dto.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static dev.leonlatsch.kolibriserver.constants.JsonResponse.*;
import static dev.leonlatsch.kolibriserver.constants.CommonResponses.*;

/**
 * Service to manage remote configuration
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Service
public class ConfigService {

    private static Logger log = LoggerFactory.getLogger(ConfigService.class);

    private HashMap<String, Object> config;
    private ObjectMapper mapper = new ObjectMapper();
    private File configFile = new File(FormatsAndFiles.CONFIG_FILE);

    @Autowired
    private AdminService adminService;

    @Autowired
    private AccessTokenService accessTokenService;

    /**
     * Get the whole config.
     *
     * @return the #config in a Container
     */
    public Container get() {
        return new Container(200, OK, config);
    }

    /**
     * Put a singe value in the config and save it.
     *
     * @param key The config key to be overwritten
     * @param value The new value
     * @param accessToken The admin access token
     * @return A container indicating success
     */
    public Container put(String key, Object value, String accessToken) {
        if (adminService.auth(accessToken)) {
            config.put(key, value);
            saveConfig();
            return RES_OK;
        } else {
            return RES_UNAUTHORIZED;
        }
    }

    public Container reset(String key, String accessToken) {
        if (adminService.auth(accessToken)) {
            config.put(key, Configs.ENABLE_REGISTRATION.getValue());
            saveConfig();
            return RES_OK;
        } else {
            return RES_UNAUTHORIZED;
        }
    }

    public String getString(String key, String defaultValue) {
        Object value = config.get(key);
        if (value instanceof String) {
            return (String) value;
        } else {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        Object value = config.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = config.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return defaultValue;
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    private void loadConfig() {
        try {
            config = mapper.readValue(configFile, HashMap.class);
        } catch (IOException e) {
            log.error("Error loading remote config: " + e);
            config = new HashMap<>();
        }
    }

    private boolean saveConfig() {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
            Files.writeString(Path.of(configFile.toURI()), json);
            return true;
        } catch (IOException e) {
            log.error("Error writing remote config: " + e);
            return false;
        }
    }
}
