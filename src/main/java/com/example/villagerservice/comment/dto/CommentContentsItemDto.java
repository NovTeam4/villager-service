package com.example.villagerservice.comment.dto;

import com.example.villagerservice.common.utils.StringConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentContentsItemDto {
    private Long memberCommentId;  // 회원후기번호
    private Long memberId;          //회원번호
    private Long score;             //평점
    private String contents;         //후기
    private Long otherId;           // 상대방 아이디
    private String created_at;      // 작성시간


    public CommentContentsItemDto(Long memberCommentId, Long memberId, Long score, String contents, Long otherId, LocalDateTime localDateTime) {
        this.memberCommentId = memberCommentId;
        this.memberId = memberId;
        this.score = score;
        this.contents = contents;
        this.otherId = otherId;
        this.created_at = StringConverter.localDateToShortLocalDateTimeString(localDateTime); // yy-MM-dd HH:mm:ss
    }


}






