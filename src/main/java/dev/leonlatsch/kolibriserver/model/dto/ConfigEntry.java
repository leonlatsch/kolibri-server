package dev.leonlatsch.kolibriserver.model.dto;

/**
 * DTO Containing one Config Entry
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class ConfigEntry {

    private String key;
    private Object value;

    public ConfigEntry() {
    }

    public ConfigEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
