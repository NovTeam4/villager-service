package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.MemberQueryRepository;
import com.example.villagerservice.member.dto.MemberDetail;
import com.example.villagerservice.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberQueryServiceImplTest {
    @Mock
    private MemberQueryRepository memberQueryRepository;
    @InjectMocks
    private MemberQueryServiceImpl memberQueryService;

    @Test
    @DisplayName("마이페이지 조회 시 회원이 없을 경우 테스트")
    void getMyPageNotFoundTest() {
        // given
        given(memberQueryRepository.getMyPage(anyLong()))
                .willReturn(null);

        // when
        MemberException memberException
                = assertThrows(MemberException.class, () -> memberQueryService.getMyPage(anyLong()));

        verify(memberQueryRepository, times(1)).getMyPage(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("마이페이지 조회 테스트")
    void getMyPageTest() {
        // given
        MemberDetail.Response mockResponse = MemberDetail.Response.builder()
                .memberId(1L)
                .nickName("별칭")
                .email("test@gmail.com")
                .introduce("자기소개")
                .mannerPoint(50)
                .partyRegisterCount(0L)
                .postRegisterCount(0L)
                .follow(0L)
                .follower(0L)
                .tags(Arrays.asList("#봄", "#여름"))
                .build();

        given(memberQueryRepository.getMyPage(anyLong()))
                .willReturn(mockResponse);

        // when
        MemberDetail.Response myPage = memberQueryService.getMyPage(anyLong());

        verify(memberQueryRepository, times(1)).getMyPage(anyLong());
        assertThat(myPage.getMemberId()).isEqualTo(1L);
        assertThat(myPage.getNickName()).isEqualTo("별칭");
        assertThat(myPage.getEmail()).isEqualTo("test@gmail.com");
        assertThat(myPage.getIntroduce()).isEqualTo("자기소개");
        assertThat(myPage.getMannerPoint()).isEqualTo(50);
        assertThat(myPage.getPartyRegisterCount()).isEqualTo(0L);
        assertThat(myPage.getPostRegisterCount()).isEqualTo(0L);
        assertThat(myPage.getFollow()).isEqualTo(0L);
        assertThat(myPage.getFollower()).isEqualTo(0L);
        assertThat(myPage.getTags().size()).isEqualTo(2);
        assertThat(myPage.getTags().get(0)).isEqualTo("#봄");
        assertThat(myPage.getTags().get(1)).isEqualTo("#여름");
        assertThat(myPage.isFollowState()).isEqualTo(false);
    }

    @Test
    @DisplayName("상대페이지 조회 시 회원이 없을 경우 테스트")
    void getOtherMyPageNotFoundTest() {
        // given
        given(memberQueryRepository.getMyPage(anyLong()))
                .willReturn(null);

        // when
        MemberException memberException
                = assertThrows(MemberException.class, () -> memberQueryService.getOtherMyPage(1L, 1L));

        verify(memberQueryRepository, times(1)).getMyPage(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("상대 마이페이지 조회 테스트")
    void getOtherMyPageTest() {
        // given
        MemberDetail.Response mockResponse = MemberDetail.Response.builder()
                .memberId(1L)
                .nickName("별칭")
                .email("test@gmail.com")
                .introduce("자기소개")
                .mannerPoint(50)
                .partyRegisterCount(0L)
                .postRegisterCount(0L)
                .follow(0L)
                .follower(0L)
                .tags(Arrays.asList("#봄", "#여름"))
                .build();

        given(memberQueryRepository.getMyPage(anyLong()))
                .willReturn(mockResponse);
        given(memberQueryRepository.getFollowState(anyLong(), anyLong()))
                .willReturn(true);

        // when
        MemberDetail.Response myPage = memberQueryService.getOtherMyPage(1L, 1L);

        verify(memberQueryRepository, times(1)).getMyPage(anyLong());
        verify(memberQueryRepository, times(1)).getFollowState(anyLong(), anyLong());
        assertThat(myPage.getMemberId()).isEqualTo(1L);
        assertThat(myPage.getNickName()).isEqualTo("별칭");
        assertThat(myPage.getEmail()).isEqualTo("test@gmail.com");
        assertThat(myPage.getIntroduce()).isEqualTo("자기소개");
        assertThat(myPage.getMannerPoint()).isEqualTo(50);
        assertThat(myPage.getPartyRegisterCount()).isEqualTo(0L);
        assertThat(myPage.getPostRegisterCount()).isEqualTo(0L);
        assertThat(myPage.getFollow()).isEqualTo(0L);
        assertThat(myPage.getFollower()).isEqualTo(0L);
        assertThat(myPage.getTags().size()).isEqualTo(2);
        assertThat(myPage.getTags().get(0)).isEqualTo("#봄");
        assertThat(myPage.getTags().get(1)).isEqualTo("#여름");
        assertThat(myPage.isFollowState()).isEqualTo(true);
    }
}