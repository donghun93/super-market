package com.devwinter.supermarket.admin.role.command.exception;

import com.devwinter.supermarket.common.SuperMarketException;

public class RoleException extends SuperMarketException {

    public RoleException(RoleErrorCode roleErrorCode) {
        super(roleErrorCode.name(), roleErrorCode.getDescription());
    }
}
