package de.leonlatsch.oliviabackend.constants;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {
    UNDEFINED(0), TEXT(1), PICTURE(2), VIDEO(3), AUDIO(4);

    private int value;
    private static Map map = new HashMap<>();

    private MessageType(int value) {
        this.value = value;
    }

    static {
        for (MessageType messageType : MessageType.values()) {
            map.put(messageType.value, messageType);
        }
    }

    public static MessageType valueOf(int messageType) {
        return (MessageType) map.get(messageType);
    }

    public int getValue() {
        return value;
    }
}
