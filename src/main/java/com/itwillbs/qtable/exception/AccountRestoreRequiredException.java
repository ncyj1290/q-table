package com.itwillbs.qtable.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountRestoreRequiredException extends AuthenticationException {
    public AccountRestoreRequiredException(String message) {
        super(message);
    }
}