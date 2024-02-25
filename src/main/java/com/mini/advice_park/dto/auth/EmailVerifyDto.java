package com.mini.advice_park.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmailVerifyDto(
        @JsonProperty("email")
        String email,

        @JsonProperty("code")
        String code
) {
}
