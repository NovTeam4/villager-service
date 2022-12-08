package com.example.villagerservice.member.dto;

import com.example.villagerservice.member.domain.Birthday;
import com.example.villagerservice.member.domain.Gender;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.valid.DigitLength;
import com.example.villagerservice.member.valid.Genders;
import com.example.villagerservice.member.valid.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;


public class CreateMember {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "닉네임은 필수입력 값입니다.")
        private String nickname;

        @Email(message = "유효하지 않은 이메일 형식입니다.",
                regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
        private String email;

        @Password
        private String password;

        @Genders
        private String gender;

        @DigitLength(min = 1000, max = 9999, message = "년도를 확인해주세요.")
        private int year;

        @DigitLength(min = 1, max = 12, message = "월을 확인해주세요.")
        private int month;

        @DigitLength(min = 1, max = 31, message = "일을 확인해주세요.")
        private int day;

        public void passwordEncrypt(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(this.password);
        }

        public Member toEntity() {
            return Member.builder()
                    .nickname(this.nickname)
                    .email(this.email)
                    .encodedPassword(this.password)
                    .gender(Gender.valueOf(this.gender))
                    .birthday(new Birthday(year, month, day))
                    .build();
        }
    }
}
