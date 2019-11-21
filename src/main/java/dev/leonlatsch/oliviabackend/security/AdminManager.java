package dev.leonlatsch.oliviabackend.security;

import dev.leonlatsch.oliviabackend.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class AdminManager {

    private static final Logger log = LoggerFactory.getLogger(AdminManager.class);

    private static String ADMIN_ACCESS_TOKEN;
    private static final String AAT_FILE = "admin-access-token.properties";
    private static final String PROPERTY_KEY = "admin.access_token";
    private static final String PROPERTY_COMMENT = "Use this token to use admin features";

    @EventListener(ContextRefreshedEvent.class)
    public static String getAdminAccessToken() {
        loadAdminAccessToken();
        return ADMIN_ACCESS_TOKEN;
    }

    private static void loadAdminAccessToken() {
        if (ADMIN_ACCESS_TOKEN != null) {
            return;
        }

        File file = new File(AAT_FILE);

        if (!file.exists()) {
            try {
                file.createNewFile();

                String token = CommonUtils.genSafeAccessToken();
                Properties properties = new Properties();
                properties.setProperty(PROPERTY_KEY, token);
                properties.store(new FileOutputStream(AAT_FILE), PROPERTY_COMMENT);
                log.info("Generated admin access token: " + token + " Please note this down. It won't be logged again");
                log.info("If you lost your access token, look for \"" + AAT_FILE + "\" or simply recreate te container");
                ADMIN_ACCESS_TOKEN = properties.getProperty(PROPERTY_KEY);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(AAT_FILE));
                ADMIN_ACCESS_TOKEN = properties.getProperty(PROPERTY_KEY);
                log.info("Loaded admin credentials");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
