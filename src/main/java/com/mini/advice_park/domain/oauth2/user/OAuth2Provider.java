package com.mini.advice_park.domain.oauth2.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    /**
     * Oauth2 서비스를 구분하기 위한 Enum
     */

    GOOGLE("google");

    private final String registrationId;
}
