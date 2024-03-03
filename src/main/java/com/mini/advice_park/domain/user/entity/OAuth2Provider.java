package com.mini.advice_park.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OAuth2Provider
 */
@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    LOCAL("local"),
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String registrationId;

}
