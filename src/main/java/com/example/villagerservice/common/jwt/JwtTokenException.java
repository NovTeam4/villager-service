package com.example.villagerservice.common.jwt;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class JwtTokenException extends VillagerException {
    private final JwtTokenErrorCode memberErrorCode;

    public JwtTokenException(JwtTokenErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.memberErrorCode = errorCode;
    }
}