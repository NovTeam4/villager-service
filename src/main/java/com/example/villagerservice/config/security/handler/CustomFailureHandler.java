package com.example.villagerservice.config.security.handler;

import com.example.villagerservice.common.exception.AuthVillagerException;
import com.example.villagerservice.common.exception.ErrorResponse;
import com.example.villagerservice.member.exception.MemberErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        ErrorResponse errorResponse = createErrorResponse((AuthVillagerException) e);
        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }

    private ErrorResponse createErrorResponse(AuthVillagerException e) {
        return ErrorResponse.builder()
                .errorCode(e.getCommonErrorCode().getErrorCode())
                .errorMessage(e.getCommonErrorCode().getErrorMessage())
                .build();
    }
}
