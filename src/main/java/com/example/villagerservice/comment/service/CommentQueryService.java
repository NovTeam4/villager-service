package com.example.villagerservice.comment.service;

import com.example.villagerservice.comment.dto.CommentDto;
import org.springframework.web.bind.annotation.RequestParam;

public interface CommentQueryService {
    //                      회원후기번호     회원번호           <평점,후기내용>          상대방아이디
    void commentCreate(Long memberId,  CommentDto.CommentRequest request);

    CommentDto.findByAllResponse getCommentList();

    CommentDto.findByNameResponse getFindName(String name);

    CommentDto.findPagingResponse findPagingComment(Long page);
}
