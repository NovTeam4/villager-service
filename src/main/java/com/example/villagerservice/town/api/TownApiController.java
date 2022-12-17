package com.example.villagerservice.town.api;

import com.example.villagerservice.town.dto.TownList;
import com.example.villagerservice.town.service.TownQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/towns")
@RequiredArgsConstructor
public class TownApiController {

    private final TownQueryService townQueryService;

    @PostMapping("/location")
    public TownList.Response getTownListWithLocation(@Valid @RequestBody TownList.LocationRequest townListLocationRequest) {
        return townQueryService.getTownListWithLocation(townListLocationRequest);
    }

    @PostMapping("/name")
    public TownList.Response getTownListName(@Valid @RequestBody TownList.NameRequest nameRequest) {
        return townQueryService.getTownListWithName(nameRequest);
    }
}
