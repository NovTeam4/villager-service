package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.domain.MemberTown;
import com.example.villagerservice.member.domain.MemberTownRepository;
import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.dto.UpdateMemberTown;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.town.domain.Town;
import com.example.villagerservice.town.domain.TownRepository;
import com.example.villagerservice.town.exception.TownException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.villagerservice.member.exception.MemberErrorCode.*;
import static com.example.villagerservice.town.exception.TownErrorCode.TOWN_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberTownServiceImplTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberTownRepository memberTownRepository;
    @Mock
    private TownRepository townRepository;
    @InjectMocks
    private MemberTownServiceImpl memberTownService;

    @Test
    @DisplayName("회원 동네 추가 시 회원이 없는 경우 테스트")
    void addMemberTownMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException
                = assertThrows(MemberException.class,
                () -> memberTownService.addMemberTown(1L, any()));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("회원 동네 추가 시 동네가 없는 경우 테스트")
    void addMemberTownNotFoundTest() {
        // given
        CreateMemberTown.Request request = CreateMemberTown.Request.builder()
                .townId(1L)
                .build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));
        given(townRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        TownException townException
                = assertThrows(TownException.class,
                () -> memberTownService.addMemberTown(1L, request));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(townRepository, times(1)).findById(anyLong());
        assertThat(townException.getTownErrorCode()).isEqualTo(TOWN_NOT_FOUND);
        assertThat(townException.getErrorCode()).isEqualTo(TOWN_NOT_FOUND.getErrorCode());
        assertThat(townException.getErrorMessage()).isEqualTo(TOWN_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("회원 동네 추가 시 개수 초과 테스트")
    void addMemberTownOverTest() {
        // given
        CreateMemberTown.Request request = CreateMemberTown.Request.builder()
                .townId(1L)
                .build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));
        given(memberTownRepository.countByMember(any()))
                .willReturn(2L);

        // when
        MemberException memberException
                = assertThrows(MemberException.class,
                () -> memberTownService.addMemberTown(1L, request));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberTownRepository, times(1)).countByMember(any());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_TOWN_ADD_MAX);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_TOWN_ADD_MAX.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_TOWN_ADD_MAX.getErrorMessage());
    }

    @Test
    @DisplayName("회원 동네 추가 시 별칭 중복 테스트")
    void addMemberTownNameDuplicateTest() {
        // given
        CreateMemberTown.Request request = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("별칭")
                .build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));
        given(memberTownRepository.existsByMemberAndTownName(any(), anyString()))
                .willReturn(true);

        // when
        MemberException memberException
                = assertThrows(MemberException.class,
                () -> memberTownService.addMemberTown(1L, request));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberTownRepository, times(1)).existsByMemberAndTownName(any(), anyString());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_TOWN_NAME_DUPLICATE);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_TOWN_NAME_DUPLICATE.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_TOWN_NAME_DUPLICATE.getErrorMessage());
    }

    @Test
    @DisplayName("회원 동네 추가 테스트")
    void addMemberTownTest() {
        // given
        CreateMemberTown.Request request = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("우리집")
                .longitude(127.53215)
                .latitude(32.890)
                .build();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();
        Town town = Town.builder()
                .city("서울")
                .town("마포구")
                .village("공덕동")
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(memberTownRepository.countByMember(any()))
                .willReturn(0L);
        given(townRepository.findById(anyLong()))
                .willReturn(Optional.of(town));

        ArgumentCaptor<MemberTown> captor = ArgumentCaptor.forClass(MemberTown.class);

        // when
        memberTownService.addMemberTown(1L, request);

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberTownRepository, times(1)).countByMember(any());
        verify(townRepository, times(1)).findById(anyLong());
        verify(memberTownRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(captor.getValue().getMember().getMemberDetail().getNickname())
                .isEqualTo(member.getMemberDetail().getNickname());
        assertThat(captor.getValue().getMember().hashCode()).isEqualTo(member.hashCode());
        assertThat(captor.getValue().getTown().getCity()).isEqualTo("서울");
        assertThat(captor.getValue().getTown().getTown()).isEqualTo("마포구");
        assertThat(captor.getValue().getTown().getVillage()).isEqualTo("공덕동");
        assertThat(captor.getValue().getTown().hashCode()).isEqualTo(town.hashCode());
        assertThat(captor.getValue().getTownName()).isEqualTo("우리집");
        assertThat(captor.getValue().getTownLocation().getLongitude()).isEqualTo(127.53215);
        assertThat(captor.getValue().getTownLocation().getLatitude()).isEqualTo(32.890);
    }

    @Test
    @DisplayName("별칭 변경 시 회원동네가 존재하지 않을경우 테스트")
    void updateMemberTownNameNotFoundMemberTownTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));
        given(memberTownRepository.getMemberTownWithMember(anyLong()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException =
                assertThrows(MemberException.class,
                        () -> memberTownService.updateMemberTown(1L,1L, any()));

        // then
        verify(memberTownRepository, times(1)).getMemberTownWithMember(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_TOWN_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_TOWN_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_TOWN_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("별칭 변경 시 회원 권한이 없는 경우 테스트")
    void updateMemberTownNameMemberNotAccessTownTest() {
        // given
        Member originalMember = Member.builder()
                .build();
        originalMember.setJwtMemberId(1L);
        MemberTown memberTown = MemberTown.createMemberTown(
                originalMember,
                null,
                null,
                null
        );
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(originalMember));

        given(memberTownRepository.getMemberTownWithMember(anyLong()))
                .willReturn(Optional.of(memberTown));

        // when
        MemberException memberException =
                assertThrows(MemberException.class,
                        () -> memberTownService.updateMemberTown(2L,1L, any()));
        
        // then
        verify(memberTownRepository, times(1)).getMemberTownWithMember(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_MATCH_REQUEST);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_MATCH_REQUEST.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_MATCH_REQUEST.getErrorMessage());
    }

    @Test
    @DisplayName("별칭 변경 테스트")
    void updateMemberTownNameTest() {
        // given
        Member origianlMember = Member.builder().build();
        origianlMember.setJwtMemberId(1L);
        MemberTown memberTown = MemberTown.createMemberTown(origianlMember, null, "hello", null);
        MemberTown memberTownMock = spy(memberTown);

        UpdateMemberTown.Request request = UpdateMemberTown.Request.builder()
                .townName("동네이름변경")
                .build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(origianlMember));
        given(memberTownRepository.getMemberTownWithMember(anyLong()))
                .willReturn(Optional.of(memberTownMock));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // when
        memberTownService.updateMemberTown(1L,1L, request);

        // then
        verify(memberTownRepository, times(1)).getMemberTownWithMember(anyLong());
        verify(memberTownMock, times(1)).updateMemberTown(captor.capture(), anyBoolean());
        assertThat(captor.getValue()).isEqualTo("동네이름변경");
    }

    @Test
    @DisplayName("등록된 회원 동네에 대해서 삭제 시 회원동네가 없을 경우 테스트")
    void deleteMemberTownNotFoundTest() {
        // given
        given(memberTownRepository.getMemberTownWithMember(anyLong()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException
                = assertThrows(MemberException.class,
                () -> memberTownService.deleteMemberTown(1L, anyLong()));

        // then
        verify(memberTownRepository, times(1)).getMemberTownWithMember(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_TOWN_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_TOWN_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_TOWN_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("등록된 회원 동네에 대해서 삭제 시 권한이 없는 경우 테스트")
    void deleteMemberTownMemberNotAccessTownTest() {
        // given
        Member originalMember = Member.builder()
                .build();
        originalMember.setJwtMemberId(1L);
        MemberTown memberTown = MemberTown.createMemberTown(
                originalMember,
                null,
                null,
                null
        );
        given(memberTownRepository.getMemberTownWithMember(anyLong()))
                .willReturn(Optional.of(memberTown));

        // when
        MemberException memberException =
                assertThrows(MemberException.class,
                        () -> memberTownService.deleteMemberTown(2L, anyLong()));

        // then
        verify(memberTownRepository, times(1)).getMemberTownWithMember(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_MATCH_REQUEST);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_MATCH_REQUEST.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_MATCH_REQUEST.getErrorMessage());
    }
    
    @Test
    @DisplayName("등록된 회원 동네에 대해서 삭제 테스트")
    void deleteMemberTownTest() {
        // given
        Member originalMember = Member.builder()
                .build();
        originalMember.setJwtMemberId(1L);
        MemberTown memberTown = MemberTown.createMemberTown(originalMember, null, "서울", null);
        given(memberTownRepository.getMemberTownWithMember(anyLong()))
                .willReturn(Optional.of(memberTown));

        ArgumentCaptor<MemberTown> captor = ArgumentCaptor.forClass(MemberTown.class);
        // when
        memberTownService.deleteMemberTown(1L, anyLong());

        // then
        verify(memberTownRepository, times(1)).getMemberTownWithMember(anyLong());
        verify(memberTownRepository, times(1)).delete(captor.capture());
        assertThat(captor.getValue().hashCode()).isEqualTo(memberTown.hashCode());
        assertThat(captor.getValue().getMember().getId()).isEqualTo(1L);
    }
}