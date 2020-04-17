package dev.leonlatsch.kolibriserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.leonlatsch.kolibriserver.constants.FormatsAndFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Service to manage configuration
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

    public void putString(String key, String value) {
        config.put(key, value);
        saveConfig();
    }

    public void putInt(String key, int value) {
        config.put(key, value);
        saveConfig();
    }

    public void putBoolean(String key, boolean value) {
        config.put(key, value);
        saveConfig();
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
            String json = mapper.writeValueAsString(config);
            Files.writeString(Path.of(configFile.toURI()), json);
            return true;
        } catch (IOException e) {
            log.error("Error writing remote config: " + e);
            return false;
        }
    }
}
