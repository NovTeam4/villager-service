package com.example.document.member;

import com.example.document.AbstractBaseRestDocumentationTemplate;
import com.example.villagerservice.member.dto.CreateMember;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class CreateMemberDocumentationTemplate extends AbstractBaseRestDocumentationTemplate {
    public CreateMemberDocumentationTemplate() {
        super(
                "회원가입 api",
                Arrays.asList(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("비밀번호"),
                        fieldWithPath("gender").description("성별"),
                        fieldWithPath("year").description("년"),
                        fieldWithPath("month").description("월"),
                        fieldWithPath("day").description("일")
                ),
                CreateMember.Request.class.getName());
    }
}
