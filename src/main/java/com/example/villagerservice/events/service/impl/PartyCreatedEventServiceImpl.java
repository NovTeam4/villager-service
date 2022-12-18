package com.example.villagerservice.events.service.impl;

import com.example.villagerservice.config.events.Events;
import com.example.villagerservice.events.service.PartyCreatedEventService;
import com.example.villagerservice.party.domain.PartyCreatedEvent;
import com.example.villagerservice.party.domain.PartyTag;
import com.example.villagerservice.town.domain.TownQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        Long townCode = getTownId(latitude, longitude);
        if(townCode != null) {
            log.info("PartyCreatedEventServiceImpl raise");
            List<String> partyTags = getPartyTags(tags);
            Events.raise(PartyCreatedEvent.createEvent(townCode,
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

    private Long getTownId(Double latitude, Double longitude) {
        return townQueryRepository.getTownId(latitude, longitude);
    }
}
