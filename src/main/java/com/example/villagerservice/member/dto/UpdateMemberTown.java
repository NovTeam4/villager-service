package com.example.villagerservice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

public class UpdateMemberTown {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @Size(min = 2, max = 8, message = "동네 별칭은 2~8글자 사이로 입력해주세요.")
        private String townName;
        private boolean main;
    }
}
