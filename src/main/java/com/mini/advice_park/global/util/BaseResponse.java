package com.mini.advice_park.global.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class BaseResponse<T> {

    /**
     * 모든 API 응답을 나타내는 범용 클래스
     */

    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    /**
     * 일반 응답
     */
    public BaseResponse(HttpStatus status, String message, T result){
        this.message = message;
        this.code = status.value();
        this.result = result;
    }

    /**
     * 오류 응답
     */
    public BaseResponse(HttpStatus status) {
        this.message = status.getReasonPhrase();
        this.code = status.value();
        this.result = null;
    }
}
