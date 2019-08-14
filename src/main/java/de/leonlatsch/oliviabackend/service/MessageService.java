package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.MessageDTO;
import de.leonlatsch.oliviabackend.entity.Message;
import de.leonlatsch.oliviabackend.repository.MessageRepository;
import de.leonlatsch.oliviabackend.util.CommonUtils;
import de.leonlatsch.oliviabackend.util.DatabaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static de.leonlatsch.oliviabackend.constants.JsonResponse.*;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private DatabaseMapper databaseMapper = DatabaseMapper.getInstance();

    @Autowired
    private MessageRepository repository;

    public MessageDTO getMessage(String mid) {
        Optional<Message> message = repository.findById(mid);
        return message.isPresent() ? databaseMapper.mapToTransferObject(message.get()) : null;
    }

    public String createMessage(MessageDTO message) {
        Message entity = databaseMapper.mapToEntity(message);
        entity.setMid(CommonUtils.genUUID());
        return repository.saveAndFlush(entity) != null ? OK : ERROR;
    }

    public String deleteMessage(String mid) {
        Optional<Message> message = repository.findById(mid);

        if (message.isPresent()) {
            repository.deleteById(mid);
            return OK;
        } else {
            return ERROR;
        }
    }
}
