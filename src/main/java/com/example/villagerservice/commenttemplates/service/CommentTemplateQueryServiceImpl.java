package com.example.villagerservice.commenttemplates.service;

import com.example.villagerservice.commenttemplates.domain.CommentTemplateQueryRepository;
import com.example.villagerservice.commenttemplates.dto.CommentTemplateDto;
import com.example.villagerservice.commenttemplates.dto.CommentTemplateItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentTemplateQueryServiceImpl implements CommentTemplateQueryService {
    private final CommentTemplateQueryRepository commentTemplateQueryRepository;
    @Override
    public CommentTemplateDto.Response getCommentTemplateList() {
        List<CommentTemplateItemDto> commentTemplate = commentTemplateQueryRepository.getCommentTemplate();
        Long commentTemplateTotalCount = commentTemplateQueryRepository.getCommentTemplateTotalCount();

        return new CommentTemplateDto.Response(commentTemplateTotalCount, commentTemplate);

    }
}
