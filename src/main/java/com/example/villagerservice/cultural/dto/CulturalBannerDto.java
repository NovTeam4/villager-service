package com.example.villagerservice.cultural.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class CulturalBannerDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private int totalCount;
        private List<CulturalBannerInfo> banners = new ArrayList<>();
    }

    @Data
    public static class CulturalBannerInfo {
        private Long culturalId;
        private String codeName;
        private String title;
        private String guName;
        private String place;
        private String orgLink;
        private String mainImage;
        private String startDate;
        private String endDate;
        private String date;

        public CulturalBannerInfo(Long culturalId, String codeName, String title, String guName, String place, String orgLink, String mainImage, String startDate, String endDate) {
            this.culturalId = culturalId;
            this.codeName = codeName;
            this.title = title;
            this.guName = guName;
            this.place = place;
            this.orgLink = orgLink;
            this.mainImage = mainImage;
            this.startDate = startDate;
            this.endDate = endDate;
            this.date = String.format("%s ~ %s", startDate, endDate);
        }
    }
}
