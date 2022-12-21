package com.example.villagerservice.post.exception;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class CategoryException extends VillagerException {
    private final CategoryErrorCode categoryErrorCode;
    public CategoryException(CategoryErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.categoryErrorCode = errorCode;
    }
}