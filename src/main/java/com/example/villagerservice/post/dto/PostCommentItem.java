package com.example.villagerservice.post.dto;

import com.example.villagerservice.common.utils.StringConverter;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostCommentItem {
    private Long commentId;
    private Long memberId;
    private String nickname;
    private String createdAt;

    public PostCommentItem(Long commentId, Long memberId, String nickname, LocalDateTime createAt) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.createdAt = StringConverter.localDateTimeToLocalDateTimeString(createAt);
    }
}
