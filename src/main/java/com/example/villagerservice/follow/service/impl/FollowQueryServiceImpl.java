package com.example.villagerservice.follow.service.impl;

import com.example.villagerservice.follow.domain.FollowQueryRepository;
import com.example.villagerservice.follow.dto.FollowList;
import com.example.villagerservice.follow.service.FollowQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowQueryServiceImpl implements FollowQueryService {

    private final FollowQueryRepository followQueryRepository;

    @Override
    public FollowList.Response getFollowList(FollowList.Request request) {
        return followQueryRepository.getFollowList(request);
    }
}
