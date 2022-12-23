package com.example.villagerservice.town.domain;

import com.example.villagerservice.town.dto.TownList;
import com.example.villagerservice.town.dto.TownListDetail;

public interface TownQueryRepository {
    TownList.Response getTownListWithLocation(TownList.LocationRequest locationRequest);
    TownList.Response getTownListWithName(TownList.NameRequest nameRequest);
    TownListDetail getTownInfo(Double latitude, Double longitude);
}
