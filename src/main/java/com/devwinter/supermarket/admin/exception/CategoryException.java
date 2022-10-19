package com.devwinter.supermarket.admin.exception;

import com.devwinter.supermarket.common.exception.SuperMarketException;
import lombok.Getter;

@Getter
public class CategoryException extends SuperMarketException {

    public CategoryException(CategoryErrorCode errorCode) {
        super(errorCode.toString(), errorCode.getDescription());
    }
}
