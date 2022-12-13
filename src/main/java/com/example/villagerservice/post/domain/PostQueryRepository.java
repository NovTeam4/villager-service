package com.example.villagerservice.post.domain;

import com.example.villagerservice.post.dto.ListPost;
import com.example.villagerservice.post.dto.ListPostItem;
import com.example.villagerservice.post.dto.ListPostSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {
    Page<ListPostItem> getPostList(ListPostSearchCond cond, Pageable pageable);
}
