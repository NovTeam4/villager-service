package com.example.villagerservice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class UpdateMemberInfo {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String nickname;
        @Size(max = 100, message = "자기소개는 100글자 이내로 입력해주세요.")
        private String introduce;
    }
}
