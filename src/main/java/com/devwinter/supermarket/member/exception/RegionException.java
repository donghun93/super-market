package com.devwinter.supermarket.member.exception;

import com.devwinter.supermarket.common.exception.SuperMarketException;
import lombok.Getter;

@Getter
public class RegionException extends SuperMarketException {

    public RegionException(RegionErrorCode errorCode) {
        super(errorCode.toString(), errorCode.getDescription());
    }
}
