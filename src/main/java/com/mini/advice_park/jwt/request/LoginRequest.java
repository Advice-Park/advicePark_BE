package com.mini.advice_park.jwt.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;

}
