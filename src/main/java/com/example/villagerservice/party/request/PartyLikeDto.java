package com.example.villagerservice.party.request;

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
        private String response;

        public static PartyLikeDto.Response toDto(boolean result){
            return Response.builder()
                .response(result ? "관심 모임 등록" : "관심 모임 취소")
                .build();
        }
    }

}
