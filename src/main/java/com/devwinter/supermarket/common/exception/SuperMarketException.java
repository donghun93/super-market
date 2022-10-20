package com.devwinter.supermarket.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class SuperMarketException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
}
