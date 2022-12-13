package com.example.villagerservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentPost {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Request{

        private String contents;
    }

    public static class Response{

    }
}
