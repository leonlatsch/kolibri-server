package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageDTO send(MessageDTO message) throws Exception {
        return message; //TODO: save the message to database
    }
}
