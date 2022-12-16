package com.example.villagerservice.comment.api;

import com.example.villagerservice.comment.dto.CommentDto;
import com.example.villagerservice.comment.service.CommentQueryService;
import com.example.villagerservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentQueryService queryService;

    @PostMapping("/create")
    public void commentCreate(@AuthenticationPrincipal Member member,
                              @RequestBody CommentDto.CommentRequest request) {
        queryService.commentCreate(member.getId(), request);
    }

    @GetMapping
    public CommentDto.findByAllResponse getCommentList() {
        return queryService.getCommentList();
    }

    @PostMapping
    public CommentDto.findByNameResponse getCommentFindName(@Valid @RequestBody CommentDto.CommentValidRequest request) {
        return queryService.getFindName(request.getComment());
    }

}


