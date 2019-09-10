package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.MessageDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    @Autowired
    private AmqpTemplate template;

    @Value("amq.headers")
    private String exchange;

    public void send(MessageDTO message, String routingKey) {
        System.out.println("Sending: " + message.getMid() + " to " + routingKey); // remove later
        template.convertAndSend(exchange, routingKey, message);

    }
}