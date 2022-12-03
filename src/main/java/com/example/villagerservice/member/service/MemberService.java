package com.example.villagerservice.member.service;

import com.example.villagerservice.member.request.MemberAddAttentionTag;
import com.example.villagerservice.member.request.MemberCreate;
import com.example.villagerservice.member.request.MemberInfoUpdate;
import com.example.villagerservice.member.request.MemberPasswordUpdate;

public interface MemberService {
    void createMember(MemberCreate memberCreate);
    void updateMemberInfo(String email, MemberInfoUpdate memberInfoUpdate);
    void updateMemberPassword(String email, MemberPasswordUpdate pass);
    void deleteMember(String email);
    void addAttentionTag(String email, MemberAddAttentionTag addAttentionTag);
}
