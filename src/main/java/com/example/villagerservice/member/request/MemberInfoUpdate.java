package com.example.villagerservice.member.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoUpdate {
    @NotBlank(message = "닉네임은 필수입력 값입니다.")
    private String nickname;
}
