package com.example.villagerservice.member.request;

import lombok.Data;

@Data
public class MemberLogin {
    private String email;
    private String password;
}
