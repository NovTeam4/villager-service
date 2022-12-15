package com.example.villagerservice.commenttemplates.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentTemplateItemDto {
    private Long id;
    private String templateContent;
}
