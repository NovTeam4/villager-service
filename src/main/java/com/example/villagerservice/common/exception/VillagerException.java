package com.example.villagerservice.common.exception;

import lombok.Getter;

@Getter
public class VillagerException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public VillagerException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
