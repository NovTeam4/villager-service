package com.example.villagerservice.party.dto;

import static com.example.villagerservice.party.type.PartyLikeResponseType.*;

import com.example.villagerservice.party.type.PartyLikeResponseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PartyLikeDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Response{
        private PartyLikeResponseType response;

        public static PartyLikeDto.Response toDto(boolean result){
            return Response.builder()
                .response(result ? 관심모임등록 : 관심모임취소)
                .build();
        }
    }

}
