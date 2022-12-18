package com.example.villagerservice.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final RabbitAdmin rabbitAdmin;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        rabbitAdmin.initialize();
    }
}