package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.ChatDTO;
import de.leonlatsch.oliviabackend.entity.Chat;
import de.leonlatsch.oliviabackend.repository.ChatRepository;
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
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private DatabaseMapper mapper = DatabaseMapper.getInstance();

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    public ChatDTO getChat(String cid) {
        Optional<Chat> chat = chatRepository.findById(cid);
        return chat.isPresent() ? mapper.mapToTransferObject(chat.get()) : null;
    }

    public String createChat(ChatDTO chatDTO) {
        Chat chat = mapper.mapToEntity(chatDTO);
        chat.setCid(CommonUtils.genUUID());
        return chatRepository.saveAndFlush(chat) != null ? OK : ERROR;
    }

    public String deleteChat(String cid) {
        Optional<Chat> chat = chatRepository.findById(cid);

        if (chat.isPresent()) {
            chatRepository.delete(chat.get());
            return OK;
        } else {
            return ERROR;
        }
    }
}
