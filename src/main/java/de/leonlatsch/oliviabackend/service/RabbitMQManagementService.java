package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.constants.Formats;
import de.leonlatsch.oliviabackend.dto.rabbitmq.RMQPermissions;
import de.leonlatsch.oliviabackend.dto.rabbitmq.RMQUser;
import de.leonlatsch.oliviabackend.rest.RestClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;

@Service
public class RabbitMQManagementService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQManagementService.class);

    @Autowired
    private RestClientFactory restClientFactory;

    public boolean createUser(String uid, String password) {
        try {
            Response<Void> response = restClientFactory.getRabbitMQRestService().createUser(Formats.USER_PREFIX + uid, new RMQUser(password, "")).execute();
            if (!response.isSuccessful()) {
                log.error("Error creating broker user for user " + uid + " " + response.message());
                return false;
            }
            Response<Void> permResponse = restClientFactory.getRabbitMQRestService().setPermissions(Formats.USER_PREFIX + uid, createReadOnlyPermissions(uid)).execute();
            if (!permResponse.isSuccessful()) {
                log.error("Error creating permissions for user " + uid + " " + permResponse.message());
                return false;
            }
            return true;
        } catch (IOException e) {
            log.error("Error creating broker user for user " + uid + " " + e);
            return false;
        }
    }

    public boolean deleteUser(String uid) {
        try {
            Response<Void> response = restClientFactory.getRabbitMQRestService().deleteUser(Formats.USER_PREFIX + uid).execute();
            if (!response.isSuccessful()) {
                log.error("Error deleting broker user " + uid + " " + response.message());
                return false;
            }
            return true;
        } catch (IOException e) {
            log.error("Error deleting broker user " + uid + " " + e);
            return false;
        }
    }

    public boolean changeBrokerPassword(String uid, String password) {
        if (deleteUser(uid)) {
            return createUser(uid, password);
        } else {
            return false;
        }
    }

    private RMQPermissions createReadOnlyPermissions(String uid) {
        return new RMQPermissions("", "", Formats.USER_QUEUE_PREFIX + uid);
    }
}
