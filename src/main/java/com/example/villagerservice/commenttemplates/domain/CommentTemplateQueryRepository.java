package com.example.villagerservice.commenttemplates.domain;

import com.example.villagerservice.commenttemplates.dto.CommentTemplateItemDto;

import java.util.List;

public interface CommentTemplateQueryRepository {
    List<CommentTemplateItemDto> getCommentTemplate();

    Long getCommentTemplateTotalCount();
}
