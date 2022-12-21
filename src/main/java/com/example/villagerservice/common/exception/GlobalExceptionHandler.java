package com.example.villagerservice.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static com.example.villagerservice.common.exception.CommonErrorCode.*;

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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("httpRequestMethodNotSupportedExceptionHandler : ", e);
        return ResponseEntity.ok().body(ErrorResponse.builder()
                .errorCode(HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR.getErrorCode())
                .errorMessage(HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR.getErrorMessage())
                .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableExceptionHandler : " , e);
        return ResponseEntity.ok().body(ErrorResponse.builder()
                .errorCode(HTTP_MESSAGE_NOT_READABLE.getErrorCode())
                .errorMessage(HTTP_MESSAGE_NOT_READABLE.getErrorMessage())
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        log.error("exceptionHandler : ", e);
        return ResponseEntity.ok().body(ErrorResponse.builder()
                .errorCode(SERVER_INVALID_ERROR.getErrorCode())
                .errorMessage(SERVER_INVALID_ERROR.getErrorMessage())
                .build()
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException e) {
        log.info("handleMaxUploadSizeExceededException", e);

        return ResponseEntity.ok().body(ErrorResponse.builder()
                .errorCode(MAX_UPLOAD_SIZE_EXCEEDED.getErrorCode())
                .errorMessage(MAX_UPLOAD_SIZE_EXCEEDED.getErrorMessage())
                .build()
        );
    }

}
