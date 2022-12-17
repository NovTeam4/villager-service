package com.example.villagerservice.events.service;

import com.example.villagerservice.party.domain.PartyTag;

import java.util.List;

public interface PartyCreatedEventService {
    void raise(Double latitude, Double longitude, int mannerPoint,
            int memberCount, Long partyId,
            int amount, String partyName, List<PartyTag> tags);
}
