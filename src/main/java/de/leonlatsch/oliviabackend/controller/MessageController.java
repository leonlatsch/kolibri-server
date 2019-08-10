package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.dto.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(Message message) throws Exception {
        return new Message(message.getFrom(), message.getText());
    }
}
