package dev.leonlatsch.oliviabackend.service;

import dev.leonlatsch.oliviabackend.dto.Container;
import dev.leonlatsch.oliviabackend.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.leonlatsch.oliviabackend.constants.CommonResponses.*;

/**
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

    public Container createAndSendMessage(String accessToken, MessageDTO message) {
        String uid = accessTokenService.getUserForToken(accessToken);
        if (!uid.equals(message.getFrom())) {
            return RES_UNAUTHORIZED;
        }

        if (!userService.userExists(message.getTo())) {
            return RES_BAD_REQUEST;
        }

        brokerService.send(message);
        return RES_OK;
    }
}
