package com.example.villagerservice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateMemberTown {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @Min(value = 1, message = "동네 id는 필수 입력값 입니다.")
        private Long townId;
        @Size(min = 2, max = 8, message = "동네 별칭은 2~8글자 사이로 입력해주세요.")
        private String townName;
        @DecimalMin(value = "1.0", message = "위도 값을 확인해주세요.")
        private Double latitude;
        @DecimalMin(value = "1.0", message = "경도 값을 확인해주세요.")
        private Double longitude;
    }
}
