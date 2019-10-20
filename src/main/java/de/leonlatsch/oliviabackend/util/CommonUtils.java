package de.leonlatsch.oliviabackend.util;

import de.leonlatsch.oliviabackend.repository.AccessTokenRepository;
import de.leonlatsch.oliviabackend.repository.ChatRepository;
import de.leonlatsch.oliviabackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.UUID;

public class CommonUtils {

    private static CommonUtils object; // Singleton

    @Autowired
    private ChatRepository chatRepository;

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private AccessTokenRepository accessTokenRepository;

    public static int genSafeUid() {
        int uid = 0;

        do {
            uid = object.genUid();
        } while (object.userRepository.findUidByUid(uid).isPresent());

        return uid;
    }

    public static String genSafeCid() {
        String cid = null;

        do {
            cid = object.genUUID();
        } while (object.chatRepository.findCidByCid(cid).isPresent());

        return cid;
    }

    public static String genSaveMid() {
        return object.genUUID();
    }

    public static String genSafeAccessToken() {
        String accessToken = null;

        do {
            accessToken = object.genAccessToken();
        } while (object.accessTokenRepository.findById(accessToken).isPresent());

        return accessToken;
    }

    ///////////// MEMBER METHODS /////////////

    private String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private int genUid() {
        Random rnd = new Random();
        return 10000000 + rnd.nextInt(90000000);
    }

    private String genAccessToken() {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY-_";
        StringBuilder token = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 24; i++) {
            int rndInt = rnd.nextInt(63);
            token.append(chars.charAt(rndInt));
        }
        return token.toString();
    }
}
