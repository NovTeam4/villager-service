package com.example.villagerservice.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final RabbitAdmin rabbitAdmin;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            rabbitAdmin.initialize();
        } catch (Exception e) {
            log.error("onApplicationEvent : ", e);
        }
    }
}