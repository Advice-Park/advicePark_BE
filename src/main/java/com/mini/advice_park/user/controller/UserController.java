package com.mini.advice_park.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 정보 조회 컨트롤러
 * 필요없을 확률 높음
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class UserController {

    @GetMapping("/user")
    public String getUserInfo() {
        return "user";
    }

    @GetMapping("/admin")
    public String getAdminInfo() {
        return "admin";
    }

}
