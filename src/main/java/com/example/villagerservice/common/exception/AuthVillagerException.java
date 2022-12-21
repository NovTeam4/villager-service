package com.example.villagerservice.common.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthVillagerException extends AuthenticationException {
    
    private final AuthErrorCode commonErrorCode;

    public AuthVillagerException(AuthErrorCode commonErrorCode) {
        super(commonErrorCode.getErrorCode());
        this.commonErrorCode = commonErrorCode;
    }
}
