package com.example.villagerservice.party.domain;

import com.example.villagerservice.config.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.example.villagerservice.config.events.EventType.PARTY_CREATED_EVENT;

@Getter
public class PartyCreatedEvent extends Event {
    private final Body body;

    private PartyCreatedEvent(Long townId,
                              String townName,
                              Double latitude,
                              Double longitude,
                              int mannerPoint,
                              int memberCount,
                              Long partyId,
                              int amount,
                              String partyName,
                              List<String> tags) {
        super(PARTY_CREATED_EVENT);
        this.body = new Body(townId, townName, latitude, longitude, mannerPoint, memberCount, partyId,
                amount, partyName, tags);
    }

    public static PartyCreatedEvent createEvent(Long townId,
                                                String townName,
                                                Double latitude,
                                                Double longitude,
                                                int mannerPoint,
                                                int memberCount,
                                                Long partyId,
                                                int amount,
                                                String partyName,
                                                List<String> tags) {
        return new PartyCreatedEvent(townId, townName, latitude, longitude,
                mannerPoint, memberCount, partyId, amount, partyName, tags);
    }

    @AllArgsConstructor
    @Getter
    static class Body {
        private Long townId;
        private String townName;
        private Double latitude;
        private Double longitude;
        private int mannerPoint;
        private int memberCount;
        private Long partyId;
        private int amount;
        private String partyName;
        private List<String> tags;
    }
}
