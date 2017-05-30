package org.restbucks.wechat.bff.http.security;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = UNAUTHORIZED)
public class CannotVerifyJwtException extends RuntimeException {

    public CannotVerifyJwtException(String message) {
        super(message);
    }

    public CannotVerifyJwtException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
