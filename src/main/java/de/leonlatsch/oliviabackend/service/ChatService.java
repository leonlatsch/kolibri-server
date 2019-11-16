package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.ChatDTO;
import de.leonlatsch.oliviabackend.dto.MessageDTO;
import de.leonlatsch.oliviabackend.entity.Chat;
import de.leonlatsch.oliviabackend.repository.ChatRepository;
import de.leonlatsch.oliviabackend.util.CommonUtils;
import de.leonlatsch.oliviabackend.util.DatabaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static de.leonlatsch.oliviabackend.constants.JsonResponse.ERROR;
import static de.leonlatsch.oliviabackend.constants.JsonResponse.OK;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private DatabaseMapper mapper = DatabaseMapper.getInstance();

    @Autowired
    private ChatRepository chatRepository;

    public ChatDTO getChat(String cid) {
        Optional<Chat> chat = chatRepository.findById(cid);
        return chat.isPresent() ? mapper.mapToTransferObject(chat.get()) : null;
    }

    public String createChat(ChatDTO chatDTO) {
        Chat chat = mapper.mapToEntity(chatDTO);
        chat.setCid(CommonUtils.genSafeCid());
        return chatRepository.saveAndFlush(chat) != null ? OK : ERROR;
    }

    public String createChatFromMessage(MessageDTO messageDTO) {
        String from = messageDTO.getFrom();
        String to = messageDTO.getTo();

        Chat chat = new Chat();
        String cid;
        if (messageDTO.getCid() != null) {
            cid = messageDTO.getCid();
        } else {
            cid = CommonUtils.genSafeCid();
        }
        chat.setCid(cid);
        chat.setFirstMember(from);
        chat.setSecondMember(to);
        return chatRepository.saveAndFlush(chat) != null ? cid : ERROR;
    }

    public ChatDTO getChatFromMembers(String firstMember, String secondMember) {
        Optional<Chat> chat = chatRepository.findByFirstMemberAndSecondMember(firstMember, secondMember);
        if (chat.isPresent()) {
            return mapper.mapToTransferObject(chat.get());
        } else {
            chat = chatRepository.findByFirstMemberAndSecondMember(secondMember, firstMember);
        }
        return chat.isPresent() ? mapper.mapToTransferObject(chat.get()) : null;
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

    public boolean chatExists(String cid) {
        if (cid == null) {
            return false;
        }
        Optional<Chat> chat = chatRepository.findById(cid);
        return chat.isPresent();
    }

    public boolean chatExists(String firstMember, String secondMember) {
        Optional<Chat> chat = chatRepository.findByFirstMemberAndSecondMember(firstMember, secondMember);
        if (chat.isPresent()) {
            return true;
        } else {
            chat = chatRepository.findByFirstMemberAndSecondMember(secondMember, firstMember);
        }
        return chat.isPresent();
    }
}
