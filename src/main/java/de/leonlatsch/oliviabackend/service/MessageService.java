package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.MessageDTO;
import de.leonlatsch.oliviabackend.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository repository;

    public MessageDTO getMessage(String mid) {
        return null; //TODO
    }

    public String createMessage(MessageDTO message) {
        return null; //TODO
    }

    public String deleteMessage(String mid) {
        return null; //TODO
    }
}
