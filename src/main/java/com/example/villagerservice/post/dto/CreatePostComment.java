package com.example.villagerservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

public class CreatePostComment {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Request {
        @Size(min = 1, max = 100, message = "댓글은 1~100글자 이내에 입력해주세요.")
        private String comment;
    }

    public static class Response{

    }
}
