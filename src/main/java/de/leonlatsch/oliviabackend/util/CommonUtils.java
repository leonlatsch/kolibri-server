package de.leonlatsch.oliviabackend.util;

import java.util.Random;
import java.util.UUID;

public class CommonUtils {

    public static String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static int genUid() {
        Random rnd = new Random();
        return 10000000 + rnd.nextInt(90000000);
    }
}
