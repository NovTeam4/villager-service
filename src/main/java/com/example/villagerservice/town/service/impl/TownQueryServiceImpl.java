package com.example.villagerservice.town.service.impl;

import com.example.villagerservice.town.domain.TownQueryRepository;
import com.example.villagerservice.town.dto.TownList;
import com.example.villagerservice.town.service.TownQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TownQueryServiceImpl implements TownQueryService {

    private final TownQueryRepository townQueryRepository;

    @Override
    public TownList.Response getTownListWithLocation(TownList.LocationRequest locationRequest) {
        return townQueryRepository.getTownListWithLocation(locationRequest);
    }

    @Override
    public TownList.Response getTownListWithName(TownList.NameRequest nameRequest) {
        return townQueryRepository.getTownListWithName(nameRequest);
    }
}
