package com.mini.advice_park.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 로그인 성공 시 응답
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private final String accessToken;
    private final String refreshToken;

    public static LoginResponse of(String accessToken, String refreshToken) {
        return new LoginResponse(accessToken, refreshToken);
    }

}
