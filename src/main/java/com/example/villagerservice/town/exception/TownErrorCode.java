package com.example.villagerservice.town.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TownErrorCode {
    TOWN_NOT_FOUND("INFO-700", "동네가 존재하지 않습니다."),
    TOWN_LATITUDE_LONGITUDE_NOT_VALID("INFO-701", "위도 또는 경도 값을 확인 해주세요.")
    ;

    private final String errorCode;
    private final String errorMessage;
}
