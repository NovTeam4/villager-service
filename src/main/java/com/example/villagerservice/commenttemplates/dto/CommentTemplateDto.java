package com.example.villagerservice.commenttemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CommentTemplateDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Response{
        private Long totalCount;
        private List<CommentTemplateItemDto> templates;
    }
}
