package com.example.villagerservice.party.domain;

import com.example.villagerservice.config.events.Event;
import lombok.Getter;

import java.util.List;

import static com.example.villagerservice.config.events.EventType.PARTY_CREATED_EVENT;

@Getter
public class PartyCreatedEvent extends Event {

    private PartyCreatedEvent(List<String> tags) {
        super(PARTY_CREATED_EVENT, tags);
    }

    public static PartyCreatedEvent createEvent(List<String> tags) {
        return new PartyCreatedEvent(tags);
    }
}
