package com.example.villagerservice.comment.service;

import com.example.villagerservice.comment.dto.CommentDto;

public interface CommentQueryService {
    //                      회원후기번호     회원번호           <평점,후기내용>          상대방아이디
    void commentCreate(Long memerId, CommentDto.CommentRequest request);

    CommentDto.findByAllResponse getCommentList();

    CommentDto.findByNameResponse getFindName(String name);
}
