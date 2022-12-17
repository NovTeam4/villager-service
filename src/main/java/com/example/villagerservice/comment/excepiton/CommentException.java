package com.example.villagerservice.comment.excepiton;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class CommentException extends VillagerException {

    private final CommentErrorCode commentErrorCode;

    public CommentException(CommentErrorCode commentErrorCode) {
      super(commentErrorCode.getErrorCode(),commentErrorCode.getErrorMessage());
        this.commentErrorCode = commentErrorCode;
    }
}
