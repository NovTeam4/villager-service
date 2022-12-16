package com.example.villagerservice.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentContentsItemDto {
    private Long memberCommentId;  // 회원후기번호
    private Long memberId;          //회원번호
    private Long score;             //평점
    private String contents;         //후기
    private Long otherId;           // 상대방 아이디
}
