package com.example.villagerservice.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum PostErrorCode {

    POST_VALID_NOT("INFO-800", "요청인자가 잘못되었습니다."),
    POST_NOT_FOUND("INFO-804", "게시글의 ID가 존재하지않습니다."),
    POST_DELETE_NOT_FOUND("INFO-814", "이미 삭제 대기중인 게시글입니다."),

    COMMENT_ID_NOT_FOUND("INFO-824", "해당 댓글을 찾을수가 없습니다"),
    COMMENT_VALID_NOT("INFO-830", "요청인자가 잘못되었습니다."),
    COMMENT_ROLE_NOT("INFO-834", "본인 댓글이 아닙니다. 권한이 없습니다.")


    ;

    private final String errorCode;
    private final String errorMessage;


    PostErrorCode(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage=errorMessage;
    }

}
