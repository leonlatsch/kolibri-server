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

    public static String genAccessToken() {
        return genAccessToken(24);
    }

    public static String genAccessToken(int length) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY-_";
        StringBuilder token = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < length; i++) {
            int rndInt = rnd.nextInt(63);
            token.append(chars.charAt(rndInt));
        }
        return token.toString();
    }
}
