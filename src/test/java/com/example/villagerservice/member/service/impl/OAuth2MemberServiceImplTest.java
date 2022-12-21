package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberDetailRepository;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.domain.Tag;
import com.example.villagerservice.member.dto.*;
import com.example.villagerservice.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.villagerservice.member.exception.MemberErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberDetailRepository memberDetailRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("회원 가입 시 이메일 중복 검사 테스트")
    void createMemberDuplicateTest() {
        // given
        String nickname = "테스트입니다.";
        String email = "test@gmail.com";
        String pass = "123456789";

        CreateMember.Request createMember = createMemberRequest(nickname, email, pass);
        Member member = createMember(email, nickname, pass);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // when
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.createMember(createMember);
        });

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(memberRepository, times(1)).findByEmail(captor.capture());
        assertThat(captor.getValue()).isEqualTo(email);
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_DUPLICATE_ERROR);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_DUPLICATE_ERROR.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_DUPLICATE_ERROR.getErrorMessage());
    }

    @Test
    @DisplayName("회원 가입 시 닉네임 중복된 경우 테스트")
    void createMemberNicknameDuplicateTest() {
        // given
        String nickname = "테스트입니다.";
        String email = "test@gmail.com";
        String pass = "123456789";

        CreateMember.Request createMember = createMemberRequest(nickname, email, pass);
        Member member = createMember(email, nickname, pass);
        CreateMember.Request createMemberMock = spy(createMember);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        given(memberDetailRepository.existsByNickname(anyString()))
                .willReturn(true);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // when
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.createMember(createMember);
        });

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(memberDetailRepository, times(1)).existsByNickname(captor.capture());
        assertThat(captor.getValue()).isEqualTo(nickname);
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NICKNAME_DUPLICATE_ERROR);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NICKNAME_DUPLICATE_ERROR.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NICKNAME_DUPLICATE_ERROR.getErrorMessage());
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void createMemberTest() {
        // given
        String nickname = "테스트입니다.";
        String email = "test@gmail.com";
        String pass = "123456789";

        CreateMember.Request createMember = createMemberRequest(nickname, email, pass);
        Member member = createMember(email, nickname, pass);
        CreateMember.Request createMemberMock = spy(createMember);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        // when
        memberService.createMember(createMemberMock);

        // then
        verify(createMemberMock, times(1)).passwordEncrypt(passwordEncoder);
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(memberRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getMemberDetail().getNickname()).isEqualTo(nickname);
        assertThat(captor.getValue().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("회원정보 변경 시 회원이 없을 경우 테스트")
    void updateMemberInfoNotFoundTest() {
        // given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.updateMemberInfo("test@gamil.com", any()));

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("회원정보 닉네임만 변경 테스트")
    void updateMemberInfoNickNameTest() {
        // given
        Member member = Member.builder()
                .nickname("원래 닉네임")
                .introduce("자기소개")
                .build();

        Member mockMember = spy(member);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(mockMember));

        UpdateMemberInfo.Request updateMemberInfo = UpdateMemberInfo.Request.builder()
                .nickname("변경 닉네임")
                .introduce("ajksld")
                .build();
        ArgumentCaptor<String> nickNameCaptor = ArgumentCaptor.forClass(String.class);

        // when
        memberService.updateMemberInfo("test@gamil.com", updateMemberInfo);

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(mockMember, times(1)).updateMemberInfo(
                nickNameCaptor.capture(), anyString());
        assertThat(nickNameCaptor.getValue()).isEqualTo("변경 닉네임");
        assertThat(mockMember.getMemberDetail().getNickname()).isEqualTo("변경 닉네임");
    }

    @Test
    @DisplayName("회원정보 자기소개만 변경 테스트")
    void updateMemberInfoIntroduceTest() {
        // given
        Member member = Member.builder()
                .nickname("원래 닉네임")
                .introduce("자기소개")
                .build();

        Member mockMember = spy(member);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(mockMember));

        UpdateMemberInfo.Request updateMemberInfo = UpdateMemberInfo.Request.builder()
                .nickname("nickname")
                .introduce("안녕하세요 반갑습니다.!")
                .build();
        ArgumentCaptor<String> introduceCaptor = ArgumentCaptor.forClass(String.class);

        // when
        memberService.updateMemberInfo("test@gamil.com", updateMemberInfo);

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(mockMember, times(1)).updateMemberInfo(
                anyString(), introduceCaptor.capture());
        assertThat(introduceCaptor.getValue()).isEqualTo("안녕하세요 반갑습니다.!");
        assertThat(mockMember.getMemberDetail().getIntroduce()).isEqualTo("안녕하세요 반갑습니다.!");
    }

    @Test
    @DisplayName("회원정보 변경 테스트")
    void updateMemberInfoTest() {
        // given
        Member member = Member.builder()
                .nickname("원래 닉네임")
                .introduce("자기소개 변경 전")
                .build();

        Member mockMember = spy(member);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(mockMember));

        UpdateMemberInfo.Request updateMemberInfo = UpdateMemberInfo.Request.builder()
                .nickname("변경 닉네임")
                .introduce("자기소개 변경 후")
                .build();

        ArgumentCaptor<String> nickNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> introduceCaptor = ArgumentCaptor.forClass(String.class);

        // when
        memberService.updateMemberInfo("test@gamil.com", updateMemberInfo);

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(mockMember, times(1)).updateMemberInfo(nickNameCaptor.capture(),
                introduceCaptor.capture());
        assertThat(nickNameCaptor.getValue()).isEqualTo("변경 닉네임");
        assertThat(introduceCaptor.getValue()).isEqualTo("자기소개 변경 후");
        assertThat(mockMember.getMemberDetail().getNickname()).isEqualTo("변경 닉네임");
        assertThat(mockMember.getMemberDetail().getIntroduce()).isEqualTo("자기소개 변경 후");
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 회원이 없을 경우 테스트")
    void updateMemberPasswordNotFoundTest() {
        // given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.updateMemberPassword("test@gamil.com", any()));

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 비밀번호 데이터가 비어있을 경우 테스트")
    void updateMemberPasswordEmptyTest() {
        // given
        UpdateMemberPassword.Request passwordUpdate = UpdateMemberPassword.Request.builder().build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(Member.builder().build()));

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.updateMemberPassword("test@gamil.com", passwordUpdate));

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_VALID_NOT);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_VALID_NOT.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_VALID_NOT.getErrorMessage());
    }

    @Test
    @DisplayName("회원탈퇴 시 회원이 없을 경우 테스트")
    void deleteMemberNotFoundTest() {
        // given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.deleteMember("test@gamil.com"));

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    void deleteMemberTest() {
        // given
        Member member = Member.builder().build();
        Member mockMember = spy(member);
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(mockMember));

        // when
        memberService.deleteMember("test@gamil.com");

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(mockMember, times(1)).deleteMember();
        assertThat(mockMember.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("관심태그 추가 시 회원이 존재하지 않을 경우 테스트")
    void addAttentionTagNotFoundMemberTest() {
        // given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.addAttentionTag("test@gmail.com", any()));

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("관심태그 추가 테스트")
    void addAttentionTagTest() {
        // given
        Member member = Member.builder().build();
        CreateMemberAttentionTag.Request createMemberAttentionTag = CreateMemberAttentionTag.Request.builder()
                .tags(Arrays.asList("봄", "여름", "가을", "겨울"))
                .build();

        Member mockMember = spy(member);
        CreateMemberAttentionTag.Request mockCreateMemberAttentionTag = spy(createMemberAttentionTag);
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(mockMember));

        Class<ArrayList<Tag>> listClass =
                (Class<ArrayList<Tag>>) (Class) ArrayList.class;
        ArgumentCaptor<List<Tag>> captor = ArgumentCaptor.forClass(listClass);

        // when
        memberService.addAttentionTag("test@gmail.com", mockCreateMemberAttentionTag);

        // then
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(mockCreateMemberAttentionTag, times(1)).toEntity();
        verify(mockMember, times(1)).addMemberAttentionTag(captor.capture());
        assertThat(captor.getValue().size()).isEqualTo(4);
        assertThat(captor.getValue().get(0).getName()).isEqualTo("봄");
        assertThat(captor.getValue().get(1).getName()).isEqualTo("여름");
        assertThat(captor.getValue().get(2).getName()).isEqualTo("가을");
        assertThat(captor.getValue().get(3).getName()).isEqualTo("겨울");
    }

    @Test
    @DisplayName("회원 닉네임 유효성 검사 시 닉네임이 존재할 경우 테스트")
    void validNicknameExistTest() {
        // given
        ValidMemberNickname.Request request = ValidMemberNickname.Request.builder()
                .nickname("테스트 닉네임")
                .build();

        given(memberDetailRepository.existsByNickname(anyString()))
                .willReturn(true);

        // when
        MemberException memberException
                = assertThrows(MemberException.class, () -> memberService.validNickname(request));

        // then
        verify(memberDetailRepository, times(1)).existsByNickname(anyString());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NICKNAME_DUPLICATE_ERROR);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NICKNAME_DUPLICATE_ERROR.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NICKNAME_DUPLICATE_ERROR.getErrorMessage());
    }

    @Test
    @DisplayName("회원 닉네임 유효성 검사 통과 테스트")
    void validNicknameTest() {
        // given
        ValidMemberNickname.Request request = ValidMemberNickname.Request.builder()
                .nickname("테스트 닉네임")
                .build();

        given(memberDetailRepository.existsByNickname(anyString()))
                .willReturn(false);
        // when
        memberService.validNickname(request);

        // then
        verify(memberDetailRepository, times(1)).existsByNickname(anyString());
    }

    @Test
    @DisplayName("비밀번호 변경 시 회원이 없을경우 테스트")
    void passwordCheckValidMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.passwordCheckValid(1L, any()));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    private Member createMember(String nickname, String email, String pass) {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .encodedPassword(pass)
                .build();
    }

    private CreateMember.Request createMemberRequest(String nickname, String email, String pass) {
        return CreateMember.Request.builder()
                .nickname(nickname)
                .email(email)
                .password(pass)
                .gender("MAN")
                .birth("2022-12-07")
                .introduce("안녕하세요 반갑습니다!")
                .build();
    }
}