package com.example.villagerservice.member.service;

import com.example.villagerservice.member.dto.CreateMemberAttentionTag;
import com.example.villagerservice.member.dto.CreateMember;
import com.example.villagerservice.member.dto.UpdateMemberInfo;
import com.example.villagerservice.member.dto.UpdateMemberPassword;

public interface MemberService {
    void createMember(CreateMember.Request createMember);
    void updateMemberInfo(String email, UpdateMemberInfo.Request updateMemberInfo);
    void updateMemberPassword(String email, UpdateMemberPassword.Request pass);
    void deleteMember(String email);
    void addAttentionTag(String email, CreateMemberAttentionTag.Request addAttentionTag);
}
