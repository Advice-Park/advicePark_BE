package com.mini.advice_park.controller;

import com.mini.advice_park.annotation.SocialId;
import com.mini.advice_park.dto.auth.AuthLoginDto;
import com.mini.advice_park.dto.auth.AuthSignUpDto;
import com.mini.advice_park.dto.auth.EmailVerifyDto;
import com.mini.advice_park.dto.exception.ResponseDto;
import com.mini.advice_park.constant.Constants;
import com.mini.advice_park.service.AuthService;
import com.mini.advice_park.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    /**
     * 일반 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseDto<?> signUp(@RequestBody AuthSignUpDto authSignUpDto) {
        return ResponseDto.ok(authService.signUp(authSignUpDto));
    }

    /**
     * 일반 로그인
     */
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody AuthLoginDto authLoginDto) {
        return ResponseDto.ok(authService.login(authLoginDto));
    }

    /**
     * 로그아웃
     * 소셜로그인을 한 경우에도 가능
     */
    @GetMapping("/logout")
    public ResponseDto<?> logout(@SocialId String socialId) {
        return ResponseDto.ok(authService.logout(socialId));
    }

    /**
     * 액세스 토큰 갱신
     * 소셜 사용자도 가능
     */
    @PostMapping("/refresh")
    public ResponseDto<?> updateAccessToken(@SocialId String socialId, HttpServletRequest request) {
        String refreshToken = request.getHeader(Constants.REAUTHORIZATION);
        return ResponseDto.ok(authService.reissue(socialId, refreshToken));
    }

    /**
     * 이메일로 인증코드 전송
     */
    @GetMapping("/mail")
    public ResponseDto<String> sendCode(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        return ResponseDto.ok(emailService.sendCode(email));
    }

    /**
     * 이메일로 전송된 인증코드 검증
     */
    @PostMapping("/mail")
    public ResponseDto<Boolean> verifyCode(@RequestBody EmailVerifyDto emailVerifyDto) {
        return ResponseDto.ok(emailService.verifyCode(emailVerifyDto));
    }
}
