package com.example.villagerservice.member.service;

import com.example.villagerservice.member.request.MemberCreate;
import com.example.villagerservice.member.request.MemberInfoUpdate;

public interface MemberService {
    void createMember(MemberCreate memberCreate);

    void updateMemberInfo(String email, MemberInfoUpdate memberInfoUpdate);
    void updateMemberPassword(String email, String pass);
}
