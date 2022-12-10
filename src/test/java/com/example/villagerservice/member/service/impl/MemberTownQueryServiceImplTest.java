package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.MemberTownQueryRepository;
import com.example.villagerservice.member.dto.FindMemberTownDetail;
import com.example.villagerservice.member.dto.FindMemberTownList;
import com.example.villagerservice.member.dto.MemberTownListItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberTownQueryServiceImplTest {
    @Mock
    private MemberTownQueryRepository memberTownQueryRepository;
    @InjectMocks
    private MemberTownQueryServiceImpl memberTownQueryService;
    @Test
    @DisplayName("등록된 동네 목록 조회")
    void getMemberTownListTest() {
        // given
        FindMemberTownList.Response mockResponse = FindMemberTownList.Response.builder()
                .towns(Arrays.asList(
                        new MemberTownListItem(1L, "name1", "city1", "town1", "village1",
                                LocalDateTime.now(), LocalDateTime.now()),
                        new MemberTownListItem(2L, "name2", "city2", "town2", "village2",
                                LocalDateTime.now(), LocalDateTime.now()),
                        new MemberTownListItem(3L, "name3", "city3", "town3", "village3",
                                LocalDateTime.now(), LocalDateTime.now())
                ))
                .build();

        given(memberTownQueryRepository.getMemberTownList(anyLong()))
                .willReturn(mockResponse);

        // when
        FindMemberTownList.Response response = memberTownQueryService.getMemberTownList(anyLong());

        // then
        assertThat(response.getTowns().size()).isEqualTo(mockResponse.getTowns().size());

        assertThat(response.getTowns().get(0).hashCode()).isEqualTo(mockResponse.getTowns().get(0).hashCode());
        assertThat(response.getTowns().get(0).getMemberTownId()).isEqualTo(mockResponse.getTowns().get(0).getMemberTownId());
        assertThat(response.getTowns().get(0).getCityName()).isEqualTo(mockResponse.getTowns().get(0).getCityName());
        assertThat(response.getTowns().get(0).getTownName()).isEqualTo(mockResponse.getTowns().get(0).getTownName());
        assertThat(response.getTowns().get(0).getCreatedAt()).isEqualTo(mockResponse.getTowns().get(0).getCreatedAt());
        assertThat(response.getTowns().get(0).getModifiedAt()).isEqualTo(mockResponse.getTowns().get(0).getModifiedAt());

        assertThat(response.getTowns().get(1).hashCode()).isEqualTo(mockResponse.getTowns().get(1).hashCode());
        assertThat(response.getTowns().get(1).getMemberTownId()).isEqualTo(mockResponse.getTowns().get(1).getMemberTownId());
        assertThat(response.getTowns().get(1).getCityName()).isEqualTo(mockResponse.getTowns().get(1).getCityName());
        assertThat(response.getTowns().get(1).getTownName()).isEqualTo(mockResponse.getTowns().get(1).getTownName());
        assertThat(response.getTowns().get(1).getCreatedAt()).isEqualTo(mockResponse.getTowns().get(1).getCreatedAt());
        assertThat(response.getTowns().get(1).getModifiedAt()).isEqualTo(mockResponse.getTowns().get(1).getModifiedAt());

        assertThat(response.getTowns().get(2).hashCode()).isEqualTo(mockResponse.getTowns().get(2).hashCode());
        assertThat(response.getTowns().get(2).getMemberTownId()).isEqualTo(mockResponse.getTowns().get(2).getMemberTownId());
        assertThat(response.getTowns().get(2).getCityName()).isEqualTo(mockResponse.getTowns().get(2).getCityName());
        assertThat(response.getTowns().get(2).getTownName()).isEqualTo(mockResponse.getTowns().get(2).getTownName());
        assertThat(response.getTowns().get(2).getCreatedAt()).isEqualTo(mockResponse.getTowns().get(2).getCreatedAt());
        assertThat(response.getTowns().get(2).getModifiedAt()).isEqualTo(mockResponse.getTowns().get(2).getModifiedAt());
    }
    
    @Test
    @DisplayName("회원동네 상세 조회 테스트")
    void getMemberTownDetailTest() {
        // given
        FindMemberTownDetail.Response response = FindMemberTownDetail.Response.builder()
                .memberTownId(1L)
                .cityName("도시이름")
                .townName("별칭")
                .latitude(32.777)
                .longitude(127.777)
                .build();

        given(memberTownQueryRepository.getMemberTownDetail(anyLong()))
                .willReturn(response);

        // when
        FindMemberTownDetail.Response memberTownDetail = memberTownQueryService.getMemberTownDetail(anyLong());

        // then
        assertThat(memberTownDetail.getMemberTownId()).isEqualTo(1L);
        assertThat(memberTownDetail.getTownName()).isEqualTo("별칭");
        assertThat(memberTownDetail.getCityName()).isEqualTo("도시이름");
        assertThat(memberTownDetail.getLatitude()).isEqualTo(32.777);
        assertThat(memberTownDetail.getLongitude()).isEqualTo(127.777);
    }
}