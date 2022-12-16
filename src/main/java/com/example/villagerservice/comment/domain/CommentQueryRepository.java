package com.example.villagerservice.comment.domain;

import com.example.villagerservice.comment.dto.CommentContentsItemDto;
import com.example.villagerservice.comment.dto.CommentDto;

import java.util.List;

public interface CommentQueryRepository {

    void create(Long memberId, CommentDto.CommentRequest request); // 후기 작성

    List<CommentContentsItemDto> getCommentList(); // 후기 전체 조회

    List<CommentContentsItemDto> getCommentFindName(String comment); // 내용으로 후기 조회

    Long getCommentTotalCount(); // 후기 총 갯수

    Long getFindByNameTotalCount(String comment); // 내용과 일치한 후기 총 갯수
}
