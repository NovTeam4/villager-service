package com.example.villagerservice.town.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TownListDetail {
    private Long townId;
    private String name;
    private String code;
    private Double latitude;
    private Double longitude;

    public TownListDetail(Long townId, String city, String town, String village, String code, Double latitude, Double longitude) {
        this.townId = townId;
        this.name = String.format("%s %s %s", city, town, village);
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
