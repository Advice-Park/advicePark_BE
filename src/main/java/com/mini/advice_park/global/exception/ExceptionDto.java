package com.mini.advice_park.global.exception;

import lombok.Getter;

@Getter
public class ExceptionDto {

    private final Integer code;
    private final String message;

    /**
     * Custom Exception에 대한 dto
     * @param errorCode
     */
    public ExceptionDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    /**
     * 일반 Exception에 대한 dto
     * @param e
     */
    public ExceptionDto(Exception e) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.message = e.getMessage();
    }

    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode);
    }

}
