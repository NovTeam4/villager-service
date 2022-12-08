package com.example.villagerservice.party.dto;

import com.example.villagerservice.member.domain.MannerPoint;
import com.example.villagerservice.party.domain.Party;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime startDt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime endDt;


        private Integer amount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response{

        private String partyName;

        private Integer score;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime startDt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime endDt;

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
