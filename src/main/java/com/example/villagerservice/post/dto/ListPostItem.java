package com.example.villagerservice.post.dto;

import com.example.villagerservice.common.utils.DateCalculateConverter;
import com.example.villagerservice.common.utils.StringConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListPostItem {
    private Long postId;
    private Long categoryId;
    private String nickName;
    private Long viewCount;
    private Long likeCount;
    private String title;
    private String categoryName;
    private String createAt;
    private String nearCreateAt;

    public ListPostItem(Long postId, Long categoryId, String nickName, Long viewCount,
                        String title, String categoryName, LocalDateTime createAt, Long likeCount) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.nickName = nickName;
        this.viewCount = viewCount;
        this.createAt = StringConverter.localDateTimeToLocalDateTimeString(createAt);
        this.nearCreateAt = DateCalculateConverter.dateCalculate(createAt);
        this.title = title;
        this.categoryName = categoryName;
        this.likeCount = likeCount;
    }
}
