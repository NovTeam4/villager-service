package com.example.villagerservice.post.dto;

import com.example.villagerservice.common.utils.DateCalculateConverter;
import com.example.villagerservice.common.utils.StringConverter;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostItemDetail {
    private Long postId;
    private Long categoryId;
    private String nickName;
    private Long viewCount;
    private Long likeCount;
    private String title;
    private String contents;
    private String categoryName;
    private String createAt;
    private String nearCreateAt;
    private boolean isLike;
    private List<PostImageItem> images = new ArrayList<>();
    private List<PostCommentItem> comments = new ArrayList<>();

    public PostItemDetail(Long postId, Long categoryId, String nickName, Long viewCount,
                          String title, String contents, String categoryName, LocalDateTime createAt, Long likeCount
    , boolean isLike) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.nickName = nickName;
        this.viewCount = viewCount;
        this.createAt = StringConverter.localDateTimeToLocalDateTimeString(createAt);
        this.nearCreateAt = DateCalculateConverter.dateCalculate(createAt);
        this.title = title;
        this.contents = contents;
        this.categoryName = categoryName;
        this.likeCount = likeCount;
        this.isLike = isLike;
    }

    public void setImages(List<PostImageItem> images) {
        this.images = images;
    }

    public void setComments(List<PostCommentItem> postComments) {
        this.comments = postComments;
    }
}
