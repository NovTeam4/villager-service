package com.example.villagerservice.config.events;

import lombok.Getter;

@Getter
public abstract class Event {
    private final String eventType;
    private final long timestamp;
    private final Object body;
    public Event(EventType eventType, Object body) {
        this.eventType = eventType.toString();
        this.timestamp = System.currentTimeMillis();
        this.body = body;
    }
}
