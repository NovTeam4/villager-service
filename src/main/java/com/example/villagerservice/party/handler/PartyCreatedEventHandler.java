package com.example.villagerservice.party.handler;

import com.example.villagerservice.config.events.Event;
import com.example.villagerservice.config.events.EventProducer;
import com.example.villagerservice.party.domain.PartyCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PartyCreatedEventHandler {
    private final EventProducer producer;
    @TransactionalEventListener(
            classes = PartyCreatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(Event event) {
        producer.eventPublish(event);
    }
}
