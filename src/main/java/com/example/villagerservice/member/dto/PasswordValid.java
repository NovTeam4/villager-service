package com.example.villagerservice.member.dto;

import com.example.villagerservice.member.valid.Password;
import lombok.*;

public class PasswordValid {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @Password
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private boolean result;
        private String message;
    }
}
