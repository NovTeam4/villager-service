package com.example.villagerservice.town.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.collection.internal.PersistentList;

import java.util.ArrayList;
import java.util.List;

public class TownList {
    @Getter
    public static class LocationRequest {
        private Double latitude;
        private Double longitude;
        private int limit;
    }

    @Getter
    public static class NameRequest {

        private String name;
        private int limit;
    }

    @Getter
    @Builder
    public static class Response {
        private List<TownListDetail> details = new ArrayList<>();

        public Response(List<TownListDetail> details) {
            this.details = details;
        }
    }
}
