//package com.example.villagerservice.member.api;
//
//import com.example.villagerservice.member.request.MemberCreate;
//import com.example.villagerservice.member.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/auth")
//public class AuthApiController {
//
//    private final MemberService memberService;
//    private final PasswordEncoder passwordEncoder;
//
//    @PostMapping("/signup")
//    public void createMember(@Valid @RequestBody MemberCreate memberCreate) {
//        memberCreate.passwordEncrypt(passwordEncoder.encode(memberCreate.getPassword()));
//        memberService.createMember(memberCreate);
//    }
//}
