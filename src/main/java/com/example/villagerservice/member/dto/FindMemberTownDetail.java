package com.example.villagerservice.member.dto;

import com.example.villagerservice.common.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FindMemberTownDetail {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long memberTownId;
        private String townName;
        private String cityName;
        private Double latitude;
        private Double longitude;
        private String createdAt;
        private String modifiedAt;

        public Response(Long memberTownId, String townName, Double latitude, Double longitude,
                        String city, String town, String village,
                        LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.memberTownId = memberTownId;
            this.townName = townName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.cityName = getCityName(city, town, village);
            this.createdAt = StringConverter.localDateTimeToLocalDateTimeString(createdAt);
            this.modifiedAt = StringConverter.localDateTimeToLocalDateTimeString(modifiedAt);
        }

        private String getCityName(String city, String town, String village) {
            return String.format("%s %s %s", city, town, village);
        }
    }
}
