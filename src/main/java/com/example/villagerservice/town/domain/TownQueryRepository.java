package com.example.villagerservice.town.domain;

import com.example.villagerservice.town.dto.TownList;

public interface TownQueryRepository {
    TownList.Response getTownListWithLocation(TownList.LocationRequest locationRequest);
    TownList.Response getTownListWithName(TownList.NameRequest nameRequest);
    Long getTownId(Double latitude, Double longitude);
}
