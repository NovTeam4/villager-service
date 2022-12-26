package com.example.villagerservice.post.service;

import com.example.villagerservice.post.dto.ListPostItem;
import com.example.villagerservice.post.dto.ListPostSearchCond;
import com.example.villagerservice.post.dto.PostDetailCond;
import com.example.villagerservice.post.dto.PostItemDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {
    Page<ListPostItem> getPostList(ListPostSearchCond cond, Pageable pageable);
    PostItemDetail getPostItemDetail(PostDetailCond cond);
}
