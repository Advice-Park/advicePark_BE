package com.mini.advice_park.user.dto;

import lombok.Data;

/**
 * 회원가입 시 입력받는 DTO
 */
@Data
public class SignUpRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String nickname;
    private String image;

}
