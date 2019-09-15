package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.ChatDTO;
import de.leonlatsch.oliviabackend.dto.MessageDTO;
import de.leonlatsch.oliviabackend.dto.Response;
import de.leonlatsch.oliviabackend.entity.Message;
import de.leonlatsch.oliviabackend.repository.MessageRepository;
import de.leonlatsch.oliviabackend.util.CommonUtils;
import de.leonlatsch.oliviabackend.util.DatabaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static de.leonlatsch.oliviabackend.constants.CommonResponses.*;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private DatabaseMapper databaseMapper = DatabaseMapper.getInstance();

    private static final String MESSAGE_ID = "message";

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private RabbitMQService rabbitMQService;

    public MessageDTO getMessage(String mid) {
        Optional<Message> message = messageRepository.findById(mid);
        return message.isPresent() ? databaseMapper.mapToTransferObject(message.get()) : null;
    }

    public Response createMessage(String accessToken, MessageDTO message) {
        int uid = accessTokenService.getUserForToken(accessToken);
        if (uid != message.getFrom()) {
            return RES_ERROR;
        }
        String cid = message.getCid();
        if (!chatService.chatExists(cid)) {
            if (chatService.chatExists(message.getFrom(), message.getTo())) {
                ChatDTO chat = chatService.getChatFromMembers(message.getFrom(), message.getTo());
                cid = chat.getCid();
            } else {
                cid = chatService.createChatFromMessage(message);
                rabbitMQService.createQueue(cid, true);
            }
            message.setCid(cid);
            message.setMid(CommonUtils.genUUID());
        }

        Message entity = databaseMapper.mapToEntity(message);
        boolean success =  messageRepository.saveAndFlush(entity) != null;
        if (success) {
            rabbitMQService.send(message);
            return RES_OK;
        } else {
            return RES_ERROR;
        }
    }

    public Response deleteMessage(String mid) {
        Optional<Message> message = messageRepository.findById(mid);

        if (message.isPresent()) {
            messageRepository.delete(message.get());
            return RES_OK;
        } else {
            return RES_ERROR;
        }
    }
}
