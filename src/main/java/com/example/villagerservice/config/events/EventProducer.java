package com.example.villagerservice.config.events;

import com.example.villagerservice.party.domain.PartyCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;
    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void eventPublish(Event event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
    }
}
