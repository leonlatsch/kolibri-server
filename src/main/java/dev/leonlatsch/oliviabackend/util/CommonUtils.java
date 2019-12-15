package dev.leonlatsch.oliviabackend.util;

import dev.leonlatsch.oliviabackend.repository.AccessTokenRepository;
import dev.leonlatsch.oliviabackend.repository.UserRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

/**
 * Common util functions
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class CommonUtils {

    private static Logger log = LoggerFactory.getLogger(CommonUtils.class);

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

    public static Blob createThumbnail(Blob original) {
        try {
            byte[] bytes = original.getBytes(1L, (int) original.length());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .size(256, 256)
                    .toOutputStream(outputStream);
            bytes = outputStream.toByteArray();

            return new SerialBlob(bytes);
        } catch (SQLException | IOException e) {
            log.error("" + e);
            return null;
        }
    }

    private CommonUtils() {
    }
}
