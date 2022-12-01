package com.example.villagerservice.member.request;

import com.example.villagerservice.member.valid.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPasswordUpdate {
    @Password
    private String password;
}
