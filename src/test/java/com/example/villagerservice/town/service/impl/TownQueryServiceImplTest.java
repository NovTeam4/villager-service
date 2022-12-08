package com.example.villagerservice.town.service.impl;

import com.example.villagerservice.town.domain.TownQueryRepository;
import com.example.villagerservice.town.dto.TownList;
import com.example.villagerservice.town.dto.TownListDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TownQueryServiceImplTest {

    @Mock
    private TownQueryRepository townQueryRepository;

    @InjectMocks
    private TownQueryServiceImpl townQueryService;

    @Test
    @DisplayName("좌표 정보로 근처동네 리스트 요청 테스트")
    void getTownListWithLocationTest() {
        // given
        given(townQueryRepository.getTownListWithLocation(any()))
                .willReturn(TownList.Response.builder()
                        .towns(Arrays.asList(
                                new TownListDetail("city1", "town1", "village1", "code1",
                                        32.111, 123.155),
                                new TownListDetail("city2", "town2", "village2", "code2",
                                        32.123, 125.317),
                                new TownListDetail("city3", "town3", "village3", "code3",
                                        32.555, 127.888)
                        ))
                        .build());

        // when
        TownList.Response townListWithLocation = townQueryService.getTownListWithLocation(any());

        // then
        verify(townQueryRepository, times(1)).getTownListWithLocation(any());
        assertThat(townListWithLocation.getTotalCount()).isEqualTo(3);
        assertThat(townListWithLocation.getTowns().get(0).getName()).isEqualTo("city1 town1 village1");
        assertThat(townListWithLocation.getTowns().get(0).getCode()).isEqualTo("code1");
        assertThat(townListWithLocation.getTowns().get(0).getLatitude()).isEqualTo(32.111);
        assertThat(townListWithLocation.getTowns().get(0).getLongitude()).isEqualTo(123.155);

        assertThat(townListWithLocation.getTowns().get(1).getName()).isEqualTo("city2 town2 village2");
        assertThat(townListWithLocation.getTowns().get(1).getCode()).isEqualTo("code2");
        assertThat(townListWithLocation.getTowns().get(1).getLatitude()).isEqualTo(32.123);
        assertThat(townListWithLocation.getTowns().get(1).getLongitude()).isEqualTo(125.317);

        assertThat(townListWithLocation.getTowns().get(2).getName()).isEqualTo("city3 town3 village3");
        assertThat(townListWithLocation.getTowns().get(2).getCode()).isEqualTo("code3");
        assertThat(townListWithLocation.getTowns().get(2).getLatitude()).isEqualTo(32.555);
        assertThat(townListWithLocation.getTowns().get(2).getLongitude()).isEqualTo(127.888);
    }

    @Test
    @DisplayName("좌표 정보로 근처동네 이름 요청 테스트")
    void getTownListWithName() {
        // given
        given(townQueryRepository.getTownListWithName(any()))
                .willReturn(TownList.Response.builder()
                        .towns(Arrays.asList(
                                new TownListDetail("city1", "town1", "village1", "code1",
                                        32.111, 123.155),
                                new TownListDetail("city2", "town2", "village2", "code2",
                                        32.123, 125.317),
                                new TownListDetail("city3", "town3", "village3", "code3",
                                        32.555, 127.888)
                        ))
                        .build());

        // when
        TownList.Response townListWithLocation = townQueryService.getTownListWithName(any());

        // then
        verify(townQueryRepository, times(1)).getTownListWithName(any());
        assertThat(townListWithLocation.getTotalCount()).isEqualTo(3);
        assertThat(townListWithLocation.getTowns().get(0).getName()).isEqualTo("city1 town1 village1");
        assertThat(townListWithLocation.getTowns().get(0).getCode()).isEqualTo("code1");
        assertThat(townListWithLocation.getTowns().get(0).getLatitude()).isEqualTo(32.111);
        assertThat(townListWithLocation.getTowns().get(0).getLongitude()).isEqualTo(123.155);

        assertThat(townListWithLocation.getTowns().get(1).getName()).isEqualTo("city2 town2 village2");
        assertThat(townListWithLocation.getTowns().get(1).getCode()).isEqualTo("code2");
        assertThat(townListWithLocation.getTowns().get(1).getLatitude()).isEqualTo(32.123);
        assertThat(townListWithLocation.getTowns().get(1).getLongitude()).isEqualTo(125.317);

        assertThat(townListWithLocation.getTowns().get(2).getName()).isEqualTo("city3 town3 village3");
        assertThat(townListWithLocation.getTowns().get(2).getCode()).isEqualTo("code3");
        assertThat(townListWithLocation.getTowns().get(2).getLatitude()).isEqualTo(32.555);
        assertThat(townListWithLocation.getTowns().get(2).getLongitude()).isEqualTo(127.888);
    }
}