package com.example.villagerservice.member.domain;

import com.example.villagerservice.town.exception.TownException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.example.villagerservice.town.exception.TownErrorCode.TOWN_LATITUDE_LONGITUDE_NOT_VALID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TownLocation {

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    public TownLocation(Double latitude, Double longitude) {
        if(latitude < 0 || longitude < 0) {
            throw new TownException(TOWN_LATITUDE_LONGITUDE_NOT_VALID);
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
