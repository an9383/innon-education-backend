package com.innon.education.jwt.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String token;

    private final String message;

    public TokenRefreshException(String token, String message) {
        super(String.format("[%s] Token 갱신 실패: [%s])", token, message));
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

}