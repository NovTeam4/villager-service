package com.example.villagerservice.member;

import com.example.villagerservice.member.exception.MemberErrorCode;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.response.MemberErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_VALIDATE_ERROR;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<MemberErrorResponse> memberExceptionHandler(MemberException e) {
        log.error("memberException : ", e);
        return ResponseEntity.ok().body(createErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MemberErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidException : ", e);

        MemberErrorResponse errorResponse = createErrorResponse(MEMBER_VALIDATE_ERROR);
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addMemberValidError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.ok().body(errorResponse);
    }

    private MemberErrorResponse createErrorResponse(MemberErrorCode errorCode) {
        return MemberErrorResponse.builder()
                .errorCode(errorCode.toString())
                .errorMessage(errorCode.getErrorMessage())
                .build();
    }
}
