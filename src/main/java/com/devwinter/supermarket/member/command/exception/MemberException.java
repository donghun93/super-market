package com.devwinter.supermarket.member.command.exception;

import com.devwinter.supermarket.common.SuperMarketException;

public class MemberException extends SuperMarketException {

    public MemberException(MemberErrorCode memberErrorCode) {
        super(memberErrorCode.name(), memberErrorCode.getDescription());
    }
}
