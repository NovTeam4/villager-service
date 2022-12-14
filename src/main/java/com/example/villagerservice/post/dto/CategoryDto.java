package com.example.villagerservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Response {
        private Long categoryid;
        private String name;
    }
}
