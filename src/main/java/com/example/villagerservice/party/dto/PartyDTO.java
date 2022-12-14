package com.example.villagerservice.party.dto;

import com.example.villagerservice.member.domain.MannerPoint;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyTag;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PartyDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{
        @NotEmpty(message = "모임 이름을 입력해주세요.")
        private String partyName;

        @NotNull(message = "모임 점수를 입력해주세요.")
        private Integer score;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startDt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate endDt;


        private Integer amount;

        @Min(1)
        private Integer numberPeople;

        private String location;

        private Double latitude;

        private Double longitude;

        private String content;
        @Size(min = 1 , max = 4)
        private List<PartyTag> tagList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response{

        private String partyName;

        private Integer score;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startDt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate endDt;

        private Integer amount;

        private String nickname;

        private Integer mannerPoint;

        public static PartyDTO.Response createPartyResponse(Party party) {
            return PartyDTO.Response.builder()
                    .partyName(party.getPartyName())
                    .score(party.getScore())
                    .startDt(party.getStartDt())
                    .endDt(party.getEndDt())
                    .amount(party.getAmount())
                    .nickname(party.getMember().getMemberDetail().getNickname())
                    .mannerPoint(party.getMember().getMemberDetail().getMannerPoint().getPoint())
                    .build();
        }
    }

}
