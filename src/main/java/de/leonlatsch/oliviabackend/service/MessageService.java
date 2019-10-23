package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.ChatDTO;
import de.leonlatsch.oliviabackend.dto.MessageDTO;
import de.leonlatsch.oliviabackend.dto.Response;
import de.leonlatsch.oliviabackend.util.CommonUtils;
import de.leonlatsch.oliviabackend.util.DatabaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static de.leonlatsch.oliviabackend.constants.CommonResponses.*;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private DatabaseMapper databaseMapper = DatabaseMapper.getInstance();

    private static final String MESSAGE_ID = "message";

    @Autowired
    private ChatService chatService;

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
        String cid = message.getCid();
        if (!chatService.chatExists(cid)) {
            if (chatService.chatExists(message.getFrom(), message.getTo())) {
                ChatDTO chat = chatService.getChatFromMembers(message.getFrom(), message.getTo());
                cid = chat.getCid();
            } else {
                cid = chatService.createChatFromMessage(message);
            }
            message.setCid(cid);
            message.setMid(CommonUtils.genSaveMid());
        }

        brokerService.send(message);
        return RES_OK;
    }
}
