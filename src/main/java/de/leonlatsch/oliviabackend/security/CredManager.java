package de.leonlatsch.oliviabackend.security;

import de.leonlatsch.oliviabackend.constants.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Random;

public class CredManager {

    private static final Logger log = LoggerFactory.getLogger(CredManager.class);

    private static final String API_CRED_PATH = "api-credentials.properties";

    public static Properties loadApiCredentials() {
        genApiCreds();

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(API_CRED_PATH));
            return properties;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private static void genApiCreds() {
        File file = new File(API_CRED_PATH);

        if (!file.exists()) {
            try {
                file.createNewFile();

                Random random = new Random();
                int rToken = random.nextInt(999999999);
                int rKey = random.nextInt(999999999);

                String token = Hash.genMd5Hex(Integer.toString(rToken));
                String key = Hash.genMd5Hex(Integer.toString(rKey));

                Properties properties = new Properties();
                properties.setProperty(Values.KEY_API_TOKEN, token);
                properties.setProperty(Values.KEY_API_KEY, key);
                properties.store(new FileOutputStream(file), Values.API_CRED_COMMENT);

            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
