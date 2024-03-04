package com.mini.advice_park.global.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtDto {

    private final String accessToken;
    private final String refreshToken;

}
