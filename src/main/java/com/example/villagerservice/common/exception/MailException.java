package com.example.villagerservice.common.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class MailException extends AuthenticationException {

    private final MailErrorCode mailErrorCode;

    public MailException(MailErrorCode mailErrorCode) {
        super(mailErrorCode.getErrorCode());
        this.mailErrorCode = mailErrorCode;
    }
}
