package com.example.villagerservice.town.service;

import com.example.villagerservice.town.dto.TownList;

public interface TownQueryService {
    TownList.Response getTownListWithLocation(TownList.LocationRequest locationRequest);
    TownList.Response getTownListWithName(TownList.NameRequest nameRequest);
}
