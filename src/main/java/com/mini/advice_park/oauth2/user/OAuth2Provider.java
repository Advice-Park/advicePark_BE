package com.mini.advice_park.oauth2.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    GOOGLE("google");

    private final String registrationId;
}