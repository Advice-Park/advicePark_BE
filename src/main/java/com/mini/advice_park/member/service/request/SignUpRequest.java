package com.mini.advice_park.member.service.request;

import lombok.Data;

@Data
public class SignUpRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String nickname;
    private String image;

}
