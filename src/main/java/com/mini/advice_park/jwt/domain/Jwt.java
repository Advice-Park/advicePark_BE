package com.mini.advice_park.jwt.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Jwt {

    private final String accessToken;
    private final String refreshToken;

}