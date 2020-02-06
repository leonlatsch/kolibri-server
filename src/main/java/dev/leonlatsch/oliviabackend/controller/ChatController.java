package dev.leonlatsch.oliviabackend.controller;

import dev.leonlatsch.oliviabackend.constants.Headers;
import dev.leonlatsch.oliviabackend.dto.Container;
import dev.leonlatsch.oliviabackend.dto.MessageDTO;
import dev.leonlatsch.oliviabackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.leonlatsch.oliviabackend.util.ControllerUtils.createResponseEntity;

/**
 * Rest Controller to receive chat messages and route them to the queue
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Autowired
    private MessageService messageService;

    /**
     * Endpoint to validate and send a message to the queue
     *
     * @param accessToken Access token of the sending user
     * @param message     {@link MessageDTO} to be sent
     * @return An empty {@link Container} to indicate failure or success
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/send")
    public ResponseEntity<Container> send(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @RequestBody MessageDTO message) {
        return createResponseEntity(messageService.createAndSendMessage(accessToken, message));
    }
}
