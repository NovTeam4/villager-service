package com.example.villagerservice.comment.service;

import com.example.villagerservice.comment.domain.CommentQueryRepository;
import com.example.villagerservice.comment.dto.CommentContentsItemDto;
import com.example.villagerservice.comment.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentQueryRepository commentQueryRepository;

    @Override
    public void commentCreate(Long memberId, CommentDto.CommentRequest request) {
        commentQueryRepository.create(memberId, request);
    }

    @Override   // 후기  전체조회
    public CommentDto.findByAllResponse getCommentList() {
        Long commentTotalCount = commentQueryRepository.getCommentTotalCount();
        List<CommentContentsItemDto> commentList = commentQueryRepository.getCommentList();
        return new CommentDto.findByAllResponse(commentTotalCount, commentList);
    }

    @Override // 내용으로 후기 조회
    public CommentDto.findByNameResponse getFindName(String name) {
        Long findByNameTotalCount = commentQueryRepository.getFindByNameTotalCount(name);
        List<CommentContentsItemDto> commentFindName = commentQueryRepository.getCommentFindName(name);
        return new CommentDto.findByNameResponse(findByNameTotalCount, commentFindName);
    }
}
