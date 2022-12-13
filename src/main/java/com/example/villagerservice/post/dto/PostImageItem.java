package com.example.villagerservice.post.dto;

import lombok.Data;

@Data
public class PostImageItem {
    private Long imageId;
    private String path;
    private Long size;

    public PostImageItem(Long imageId, String path, Long size) {
        this.imageId = imageId;
        this.path = path;
        this.size = size;
    }
}
