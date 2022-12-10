package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.MemberTownQueryRepository;
import com.example.villagerservice.member.dto.FindMemberTownList;
import com.example.villagerservice.member.service.MemberTownQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberTownQueryServiceImpl implements MemberTownQueryService {

    private final MemberTownQueryRepository memberTownQueryRepository;

    @Override
    public FindMemberTownList.Response getMemberTownList(Long memberId) {
        return memberTownQueryRepository.getMemberTownList(memberId);
    }
}
