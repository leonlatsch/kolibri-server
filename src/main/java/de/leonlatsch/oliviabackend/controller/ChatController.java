package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.constants.Headers;
import de.leonlatsch.oliviabackend.dto.MessageDTO;
import de.leonlatsch.oliviabackend.dto.Response;
import de.leonlatsch.oliviabackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public ResponseEntity<Response> send(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @RequestBody MessageDTO message) {
        Response response = messageService.createMessage(accessToken, message);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
