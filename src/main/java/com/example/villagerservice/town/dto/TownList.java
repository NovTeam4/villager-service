package com.example.villagerservice.town.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class TownList {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LocationRequest {
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NameRequest {

        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private int totalCount;
        private List<TownListDetail> towns = new ArrayList<>();

        @Builder
        private Response(List<TownListDetail> towns) {
            this.totalCount = towns.size();
            this.towns = towns;
        }
    }
}
