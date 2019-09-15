package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.MessageDTO;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    public static final String EXCHANGE = "amq.direct";

    @Autowired
    private AmqpTemplate template;

    @Autowired
    private AmqpAdmin admin;

    public void send(MessageDTO message) {
        template.convertAndSend(message.getCid(), message);
    }

    public void createQueue(String queueName, boolean durable) {
        Queue queue = new Queue(queueName, durable, false, false);
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, EXCHANGE, queueName, null);
        admin.declareQueue(queue);
        admin.declareBinding(binding);
    }
}