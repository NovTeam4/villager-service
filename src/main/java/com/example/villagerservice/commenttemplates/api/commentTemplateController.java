package com.example.villagerservice.commenttemplates.api;

import com.example.villagerservice.commenttemplates.dto.CommentTemplateDto;
import com.example.villagerservice.commenttemplates.service.CommentTemplateQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class commentTemplateController {
    private final CommentTemplateQueryService commentTemplateQueryService;

    @GetMapping("/template")
    public CommentTemplateDto.Response postCommentTemplateToList() {
        return commentTemplateQueryService.getCommentTemplateList();
    }


}
