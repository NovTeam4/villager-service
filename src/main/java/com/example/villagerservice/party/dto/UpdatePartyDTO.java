package com.example.villagerservice.party.dto;

import com.example.villagerservice.party.domain.PartyTag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UpdatePartyDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{

        private String partyName;

        private Integer score;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startDt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate endDt;

        private Integer amount;

        private Integer numberPeople;

        private String location;

        private Double latitude;

        private Double longitude;

        private String content;

        private List<PartyTag> tagList;

    }
}
