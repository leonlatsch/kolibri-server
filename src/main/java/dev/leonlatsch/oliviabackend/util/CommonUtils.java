package dev.leonlatsch.oliviabackend.util;

import dev.leonlatsch.oliviabackend.repository.AccessTokenRepository;
import dev.leonlatsch.oliviabackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.UUID;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
public class CommonUtils {

    @Autowired
    private static UserRepository userRepository;

    @Autowired
    private static AccessTokenRepository accessTokenRepository;

    public static String genSafeUid() {
        String uid = null;

        if (userRepository != null) {
            do {
                uid = genUUID();
            } while (userRepository.findUidByUid(uid).isPresent());
        } else {
            uid = genUUID();
        }

        return uid;
    }

    public static String genSaveMid() {
        return genUUID();
    }

    public static String genSafeAccessToken() {
        String accessToken = null;

        if (accessTokenRepository != null) {
            do {
                accessToken = genAccessToken();
            } while (accessTokenRepository.findById(accessToken).isPresent());
        } else {
            accessToken = genAccessToken();
        }

        return accessToken;
    }

    ///////////// MEMBER METHODS /////////////

    private static String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private static String genAccessToken() {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY-_";
        StringBuilder token = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 24; i++) {
            int rndInt = rnd.nextInt(63);
            token.append(chars.charAt(rndInt));
        }
        return token.toString();
    }

    private CommonUtils() {}
}
