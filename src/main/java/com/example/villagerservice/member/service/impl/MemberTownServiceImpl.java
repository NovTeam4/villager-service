package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.*;
import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.dto.UpdateMemberTown;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.service.MemberTownService;
import com.example.villagerservice.town.domain.Town;
import com.example.villagerservice.town.domain.TownRepository;
import com.example.villagerservice.town.exception.TownException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.villagerservice.member.exception.MemberErrorCode.*;
import static com.example.villagerservice.town.exception.TownErrorCode.TOWN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberTownServiceImpl implements MemberTownService {
    private final static int MEMBER_TOWN_ADD_MAX_COUNT = 2;
    private final MemberRepository memberRepository;
    private final MemberTownRepository memberTownRepository;
    private final TownRepository townRepository;

    @Override
    public void addMemberTown(Long memberId, CreateMemberTown.Request request) {
        Member member = findMemberById(memberId);
        // 회원 동네 개수 체크
        Long count = memberTownCountValid(member);
        // 별칭 중복 검사
        memberTownNameDuplicateValid(member, request.getTownName());

        Town town = findTownById(request);
        MemberTown memberTown = createMemberTown(request, member, town);
        memberTownMainCheck(count, memberTown);
        memberTownRepository.save(memberTown);
    }

    @Override
    @Transactional
    public void updateMemberTown(Long memberId, Long memberTownId, UpdateMemberTown.Request request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_TOWN_NOT_FOUND));
        MemberTown memberTown = findMemberTownByIdWithMember(memberTownId);
        memberTownEditAccessValid(memberId, memberTown);

        List<MemberTown> memberTownList = memberTownRepository.findByMember(member);
        if(request.isMain()) {
            memberTownList.forEach(mt -> mt.updateMemberTownMain(false));
        }
        memberTown.updateMemberTown(request.getTownName(), request.isMain());
    }

    @Override
    @Transactional
    public void deleteMemberTown(Long memberId, Long memberTownId) {
        MemberTown memberTown = findMemberTownByIdWithMember(memberTownId);
        memberTownEditAccessValid(memberId, memberTown);
        memberTownRepository.delete(memberTown);
    }

    private void memberTownMainCheck(Long count, MemberTown memberTown) {
        if (count == 0) {
            memberTown.updateMemberTownMain(true);
        }
    }

    private void memberTownEditAccessValid(Long memberId, MemberTown memberTown) {
        if (!memberTown.getMember().getId().equals(memberId)) {
            throw new MemberException(MEMBER_NOT_MATCH_REQUEST);
        }
    }

    private void memberTownNameDuplicateValid(Member member, String townName) {
        if (memberTownRepository.existsByMemberAndTownName(member, townName)) {
            throw new MemberException(MEMBER_TOWN_NAME_DUPLICATE);
        }
    }

    private MemberTown findMemberTownByIdWithMember(Long memberTownId) {
        return memberTownRepository.getMemberTownWithMember(memberTownId)
                .orElseThrow(() -> new MemberException(MEMBER_TOWN_NOT_FOUND));
    }

    private MemberTown createMemberTown(CreateMemberTown.Request request, Member member, Town town) {
        return MemberTown.createMemberTown(member, town, request.getTownName(),
                new TownLocation(request.getLatitude(), request.getLongitude()));
    }

    private Town findTownById(CreateMemberTown.Request request) {
        return townRepository.findById(request.getTownId())
                .orElseThrow(() -> new TownException(TOWN_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Long memberTownCountValid(Member member) {
        Long count = memberTownRepository.countByMember(member);
        if (count >= MEMBER_TOWN_ADD_MAX_COUNT) {
            throw new MemberException(MEMBER_TOWN_ADD_MAX);
        }
        return count;
    }
}
