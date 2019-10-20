package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.constants.Headers;
import de.leonlatsch.oliviabackend.dto.MessageDTO;
import de.leonlatsch.oliviabackend.dto.Response;
import de.leonlatsch.oliviabackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static de.leonlatsch.oliviabackend.util.ControllerUtils.createResponseEntity;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public ResponseEntity<Response> send(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @RequestBody MessageDTO message) {
        return createResponseEntity(messageService.createAndSendMessage(accessToken, message));
    }
}
