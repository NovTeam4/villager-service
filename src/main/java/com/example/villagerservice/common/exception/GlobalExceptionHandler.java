package com.example.villagerservice.common.exception;

import com.example.villagerservice.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static com.example.villagerservice.common.exception.CommonErrorCode.createErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(VillagerException.class)
    public ResponseEntity<ErrorResponse> memberExceptionHandler(VillagerException e) {
        log.error("memberException : ", e);
        return ResponseEntity.ok().body(ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getErrorMessage())
                .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidException : ", e);

        ErrorResponse errorResponse = createErrorResponse(DATA_INVALID_ERROR);
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.ok().body(errorResponse);
    }
}
