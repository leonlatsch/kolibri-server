package dev.leonlatsch.oliviabackend.service;

import dev.leonlatsch.oliviabackend.dto.MessageDTO;
import dev.leonlatsch.oliviabackend.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.leonlatsch.oliviabackend.constants.CommonResponses.*;

@Service
public class MessageService {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private UserService userService;

    public Response createAndSendMessage(String accessToken, MessageDTO message) {
        String uid = accessTokenService.getUserForToken(accessToken);
        if (!uid.equals(message.getFrom())) {
            return RES_UNAUTHORIZED;
        }

        if (!userService.userExists(message.getTo())) {
            return RES_ERROR;
        }

        brokerService.send(message);
        return RES_OK;
    }
}