package com.devwinter.supermarket.member.exception;

import com.devwinter.supermarket.common.exception.SuperMarketException;
import lombok.Getter;

@Getter
public class MemberException extends SuperMarketException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.toString(), errorCode.getDescription());
    }
}
