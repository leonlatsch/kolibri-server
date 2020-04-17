package dev.leonlatsch.kolibriserver.constants;

import dev.leonlatsch.kolibriserver.model.dto.ConfigEntry;

/**
 * Config keys and default values for remote config
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class Configs {

    public static final ConfigEntry ENABLE_REGISTRATION = new ConfigEntry("enableRegistration", true);

    private Configs() {
    }
}
