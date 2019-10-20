package de.leonlatsch.oliviabackend.util;

import de.leonlatsch.oliviabackend.repository.AccessTokenRepository;
import de.leonlatsch.oliviabackend.repository.ChatRepository;
import de.leonlatsch.oliviabackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

public class CommonUtils {

    @Autowired
    private static ChatRepository chatRepository;

    @Autowired
    private static UserRepository userRepository;

    @Autowired
    private static AccessTokenRepository accessTokenRepository;

    public static int genSafeUid() {
        int uid = 0;

        if (userRepository != null) {
            do {
                uid = genUid();
            } while (userRepository.findUidByUid(uid).isPresent());
        } else {
            uid = genUid();
        }

        return uid;
    }

    public static String genSafeCid() {
        String cid = null;

        if (chatRepository != null) {
            do {
                cid = genUUID();
            } while (chatRepository.findCidByCid(cid).isPresent());
        } else {
            cid = genUUID();
        }

        return cid;
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

    private static int genUid() {
        Random rnd = new Random();
        return 10000000 + rnd.nextInt(90000000);
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
