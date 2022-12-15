package com.example.villagerservice.commenttemplates.service;

import com.example.villagerservice.commenttemplates.dto.CommentTemplateDto;

public interface CommentTemplateQueryService {
    CommentTemplateDto.Response getCommentTemplateList();
}
