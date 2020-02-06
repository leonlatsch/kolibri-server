package dev.leonlatsch.oliviabackend.constants;

/**
 * Message types for a {@link dev.leonlatsch.oliviabackend.model.dto.MessageDTO}
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class MessageType {

    public static final String UNDEFINED = "UNDEFINED";
    public static final String TEXT = "TEXT";
    public static final String PICTURE = "IMAGE";
    public static final String VIDEO = "VIDEO";
    public static final String AUDIO = "AUDIO";

    private MessageType() {
    }
}
