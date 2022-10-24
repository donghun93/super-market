package com.devwinter.supermarket.admin.exception;

import com.devwinter.supermarket.common.exception.SuperMarketException;
import lombok.Getter;

@Getter
public class NoticeException extends SuperMarketException {

    public NoticeException(NoticeErrorCode errorCode) {
        super(errorCode.toString(), errorCode.getDescription());
    }
}
