package com.mini.advice_park.user.dto;

import lombok.Data;

/**
 * 로그인 시 입력받는 DTO
 */
@Data
public class LoginRequest {

    private String username;
    private String password;

}
