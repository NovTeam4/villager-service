package com.example.villagerservice.events.service.impl;

import com.example.villagerservice.config.events.Events;
import com.example.villagerservice.events.service.PartyCreatedEventService;
import com.example.villagerservice.party.domain.PartyCreatedEvent;
import com.example.villagerservice.party.domain.PartyTag;
import com.example.villagerservice.town.domain.TownQueryRepository;
import com.example.villagerservice.town.dto.TownListDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyCreatedEventServiceImpl implements PartyCreatedEventService {
    private final TownQueryRepository townQueryRepository;

    @Override
    public void raise(Double latitude, Double longitude, int mannerPoint,
                      int memberCount,
                      Long partyId,
                      int amount,
                      String partyName,
                      List<PartyTag> tags) {
        TownListDetail townInfo = getTownInfo(latitude, longitude);
        if(townInfo != null) {
            log.info("PartyCreatedEventServiceImpl raise");
            List<String> partyTags = getPartyTags(tags);
            Events.raise(PartyCreatedEvent.createEvent(
                    townInfo.getTownId(),
                    townInfo.getName(),
                    latitude,
                    longitude,
                    mannerPoint,
                    memberCount,
                    partyId,
                    amount,
                    partyName,
                    partyTags));
        }
    }

    private List<String> getPartyTags(List<PartyTag> tags) {
        return tags.stream()
                .map(PartyTag::getTagName)
                .collect(Collectors.toList());
    }

    private TownListDetail getTownInfo(Double latitude, Double longitude) {
        return townQueryRepository.getTownInfo(latitude, longitude);
    }
}
