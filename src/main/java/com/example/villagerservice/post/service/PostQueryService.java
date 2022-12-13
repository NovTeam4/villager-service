package com.example.villagerservice.post.service;

import com.example.villagerservice.post.dto.ListPostItem;
import com.example.villagerservice.post.dto.ListPostSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {
    Page<ListPostItem> getPostList(ListPostSearchCond cond, Pageable pageable);
}
