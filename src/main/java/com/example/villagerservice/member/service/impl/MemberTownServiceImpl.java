package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.*;
import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.service.MemberTownService;
import com.example.villagerservice.town.domain.Town;
import com.example.villagerservice.town.domain.TownRepository;
import com.example.villagerservice.town.exception.TownException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_TOWN_ADD_MAX;
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
        memberTownCountValid(member);
        Town town = findTownById(request);
        memberTownRepository.save(createMemberTown(request, member, town));
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

    private void memberTownCountValid(Member member) {
        Long count = memberTownRepository.countByMember(member);
        if(count >= MEMBER_TOWN_ADD_MAX_COUNT) {
            throw new MemberException(MEMBER_TOWN_ADD_MAX);
        }
    }
}
