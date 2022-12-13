package com.example.villagerservice.party.service.impl;

import static com.example.villagerservice.party.type.PartyLikeResponseType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyLike;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyLikeRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyLikeDto;
import com.example.villagerservice.party.type.PartyLikeResponseType;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartyLikeServiceImplTest {
    @Mock
    private PartyLikeRepository partyLikeRepository;
    @Mock
    private PartyRepository partyRepository;
    @InjectMocks
    private PartyLikeServiceImpl partyLikeService;

    @Test
    @DisplayName("좋아요 성공 - 등록")
    void 좋아요_성공_등록() {
        // given
        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(Party.builder()
                .id(1L)
                .build()));
        given(partyLikeRepository.findByParty_IdAndMember_Email(anyLong(), anyString()))
            .willReturn(Optional.empty());

        // when
        PartyLikeDto.Response response = partyLikeService.partyLike(1L, Member.builder().email("123").build());

        // then
        assertEquals(관심모임등록, response.getResponse());
    }

    @Test
    @DisplayName("좋아요 성공 - 삭제")
    void 좋아요_성공_삭제() {
        // given
        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(Party.builder()
                .id(1L)
                .build()));
        given(partyLikeRepository.findByParty_IdAndMember_Email(anyLong(), anyString()))
            .willReturn(Optional.of(PartyLike.builder().build()));

        // when
        PartyLikeDto.Response response = partyLikeService.partyLike(1L, Member.builder().email("123").build());

        // then
        assertEquals(관심모임취소, response.getResponse());
    }

    @Test
    @DisplayName("좋아요 실패 - 모임이 없음")
    void 좋아요_실패_모임이없음() {
        // given
        // when
        PartyException exception = assertThrows(PartyException.class,
            () -> partyLikeService.partyLike(-1L, Member.builder().build()));

        // then
        assertEquals(PartyErrorCode.PARTY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }
}