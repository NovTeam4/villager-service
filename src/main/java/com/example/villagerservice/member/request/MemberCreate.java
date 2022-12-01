package com.example.villagerservice.member.request;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.valid.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreate {

    @NotBlank(message = "닉네임은 필수입력 값입니다.")
    private String nickname;

    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @Password
    private String password;

    public void passwordEncrypt(String encryptPassword) {
        this.password = encryptPassword;
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(this.nickname)
                .email(this.email)
                .encodedPassword(this.password)
                .build();
    }
}
