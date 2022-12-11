package com.example.villagerservice.follow.service.impl;

import com.example.villagerservice.follow.domain.FollowQueryRepository;
import com.example.villagerservice.follow.dto.FollowList;
import com.example.villagerservice.follow.dto.FollowListItem;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FollowQueryServiceImplTest {
    @Mock
    private FollowQueryRepository followQueryRepository;
    @InjectMocks
    private FollowQueryServiceImpl followQueryService;

    @Test
    @DisplayName("팔로우 랭킹 조회")
    void getFollowListTest() {
        // given
        given(followQueryRepository.getFollowList(any()))
                .willReturn(FollowList.Response.builder()
                        .pageNumber(1)
                        .follows(Arrays.asList(
                                new FollowListItem("유저1", 30L),
                                new FollowListItem("유저2", 20L),
                                new FollowListItem("유저3", 10L)
                        ))
                        .build());

        // when
        FollowList.Response response = followQueryService.getFollowList(any());

        // then
        verify(followQueryRepository, times(1)).getFollowList(any());
        assertThat(response.getPageNumber()).isEqualTo(1);
        assertThat(response.getFollows().size()).isEqualTo(3);
        assertThat(response.getFollows().get(0).getNickName()).isEqualTo("유저1");
        assertThat(response.getFollows().get(0).getFollowCount()).isEqualTo(30L);
        assertThat(response.getFollows().get(1).getNickName()).isEqualTo("유저2");
        assertThat(response.getFollows().get(1).getFollowCount()).isEqualTo(20L);
        assertThat(response.getFollows().get(2).getNickName()).isEqualTo("유저3");
        assertThat(response.getFollows().get(2).getFollowCount()).isEqualTo(10L);
    }
}