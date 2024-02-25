package com.mini.advice_park.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}