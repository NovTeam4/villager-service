package com.example.villagerservice.config.events;

import lombok.Getter;

@Getter
public abstract class Event {
    private final String eventType;
    private final long timestamp;

    public Event(EventType eventType) {
        this.eventType = eventType.toString();
        this.timestamp = System.currentTimeMillis();
    }
}
