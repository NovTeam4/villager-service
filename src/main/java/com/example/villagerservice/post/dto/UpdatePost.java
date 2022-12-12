package com.example.villagerservice.post.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class UpdatePost {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Min(value = 1, message = "카테고리 id는 필수 입력값 입니다.")
        private Long categoryId;

        @Size(max = 16, message = "게시글 제목은 16글자 이내로 작성해주세요.")
        private String title;

        @Size(max = 500, message = "게시글 제목은 500글자 이내로 작성해주세요.")
        private String contents;
    }

    public static class Response {

    }

}
