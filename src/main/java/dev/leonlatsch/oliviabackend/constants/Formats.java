package dev.leonlatsch.oliviabackend.constants;

import java.text.SimpleDateFormat;

public class Formats {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static final String USER_QUEUE_PREFIX = "queue.user.";
    public static final String USER_PREFIX = "user.";

    private Formats() {}
}