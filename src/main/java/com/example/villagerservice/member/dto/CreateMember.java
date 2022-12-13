package com.example.villagerservice.member.dto;

import com.example.villagerservice.member.domain.Birthday;
import com.example.villagerservice.member.domain.Gender;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.valid.Birth;
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
        @NotBlank(message = "닉네임은 필수입력 값이며, 공백은 포함될 수 없습니다.")
        private String nickname;

        @Email(message = "유효하지 않은 이메일 형식입니다.",
                regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
        private String email;

        @Password
        private String password;

        @Genders
        private String gender;

        @Birth
        private String birth;

        @Size(max = 100, message = "자기소개는 100글자 이내로 입력해주세요.")
        private String introduce;

        public void passwordEncrypt(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(this.password);
        }

        public Member toEntity() {
            return Member.builder()
                    .nickname(this.nickname)
                    .email(this.email)
                    .encodedPassword(this.password)
                    .gender(Gender.valueOf(this.gender))
                    .birthday(new Birthday(getYear(birth), getMonth(birth), getDay(birth)))
                    .introduce(this.introduce)
                    .build();
        }

        private int getYear(String birth) {
            return Integer.parseInt(birth.substring(0, 4));
        }

        private int getMonth(String birth) {
            return Integer.parseInt(birth.substring(5, 7));
        }

        private int getDay(String birth) {
            return Integer.parseInt(birth.substring(8, 10));
        }
    }
}
