package com.mini.advice_park.member.controller;

import com.mini.advice_park.member.service.MemberService;
import com.mini.advice_park.member.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 일반 회원가입 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpRequest request) {
        memberService.signUp(request);
    }

}
