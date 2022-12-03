package com.example.villagerservice.member.request;

import com.example.villagerservice.member.domain.Birthday;
import com.example.villagerservice.member.domain.Gender;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.valid.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

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

    private Gender gender;

    @Digits(integer = 4, fraction = 0, message = "년도를 확인해주세요.")
    private int year;

    @Min(value = 1, message = "1보다 작을 수 없습니다.")
    @Max(value = 12, message = "12보다 클 수 없습니다.")
    private int month;

    @Min(value = 1, message = "1보다 작을 수 없습니다.")
    @Max(value = 31, message = "31보다 클 수 없습니다.")
    private int day;

    public void passwordEncrypt(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(this.nickname)
                .email(this.email)
                .encodedPassword(this.password)
                .gender(this.gender)
                .birthday(new Birthday(year, month, day))
                .build();
    }
}
