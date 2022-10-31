package com.devwinter.supermarket.member.command.application.exception;

import com.devwinter.supermarket.common.exception.SuperMarketException;

public class MemberException extends SuperMarketException {

    public MemberException(MemberErrorCode memberErrorCode) {
        super(memberErrorCode.name(), memberErrorCode.getDescription());
    }
}
