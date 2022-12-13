package com.example.villagerservice.post.service.impl;

import com.example.villagerservice.post.domain.PostQueryRepository;
import com.example.villagerservice.post.dto.ListPostItem;
import com.example.villagerservice.post.dto.ListPostSearchCond;
import com.example.villagerservice.post.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService {

    private final PostQueryRepository postQueryRepository;

    @Override
    public Page<ListPostItem> getPostList(ListPostSearchCond cond, Pageable pageable) {
        return postQueryRepository.getPostList(cond, pageable);
    }
}
