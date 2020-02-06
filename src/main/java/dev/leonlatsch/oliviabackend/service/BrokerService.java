package dev.leonlatsch.oliviabackend.service;

import dev.leonlatsch.oliviabackend.constants.Formats;
import dev.leonlatsch.oliviabackend.dto.MessageDTO;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to interact with the RabbitMQ broker.
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Service
public class BrokerService {

    public static final String EXCHANGE = "amq.direct";

    @Autowired
    private AmqpTemplate template;

    @Autowired
    private AmqpAdmin admin;

    public void send(MessageDTO message) {
        template.convertAndSend(Formats.USER_QUEUE_PREFIX + message.getTo(), message);
    }

    public void createQueue(String queueName, boolean durable) {
        Queue queue = new Queue(queueName, durable, false, false);
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, EXCHANGE, queueName, null);
        admin.declareQueue(queue);
        admin.declareBinding(binding);
    }

    public void deleteQueue(String queueName) {
        admin.deleteQueue(queueName);
    }
}