package com.example.villagerservice.post.dto;

import com.example.villagerservice.common.utils.StringConverter;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostCommentItem {
    private Long id;
    private Long memberId;
    private String nickname;
    private String createdAt;

    public PostCommentItem(Long id, Long memberId, String nickname, LocalDateTime createAt) {
        this.id = id;
        this.memberId = memberId;
        this.nickname = nickname;
        this.createdAt = StringConverter.localDateTimeToLocalDateString(createAt);
    }
}
