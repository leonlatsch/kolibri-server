package dev.leonlatsch.kolibriserver.service;

import dev.leonlatsch.kolibriserver.model.dto.Container;
import dev.leonlatsch.kolibriserver.model.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.leonlatsch.kolibriserver.constants.CommonResponses.*;

/**
 * Service to handle Messages.
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Service
public class MessageService {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private UserService userService;

    /**
     * Validate an incomming message and forward it to the queue.
     *
     * @param accessToken The sending users access token
     * @param message A {@link MessageDTO}
     * @return A Container with a result
     */
    public Container createAndSendMessage(String accessToken, MessageDTO message) {
        String uid = accessTokenService.getUserForToken(accessToken);
        if (!uid.equals(message.getFrom())) {
            return RES_UNAUTHORIZED;
        }

        if (!userService.userExists(message.getTo())) {
            return RES_BAD_REQUEST;
        }

        if (!message.validate()) {
            return RES_BAD_REQUEST;
        }

        brokerService.send(message);
        return RES_OK;
    }
}
