package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.ChatDTO;
import de.leonlatsch.oliviabackend.repository.ChatRepository;
import de.leonlatsch.oliviabackend.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    public ChatDTO getChat(String cid) {
        return null; //TODO
    }

    public String createChat(ChatDTO chatDTO) {
        return null; //TODO
    }

    public String deleteChat(String cid) {
        return null; //TODO
    }
}
