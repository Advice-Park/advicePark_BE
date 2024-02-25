package com.mini.advice_park.controller;

import com.mini.advice_park.annotation.SocialId;
import com.mini.advice_park.dto.auth.AuthLoginDto;
import com.mini.advice_park.dto.auth.AuthSignUpDto;
import com.mini.advice_park.dto.auth.EmailVerifyDto;
import com.mini.advice_park.dto.exception.ResponseDto;
import com.mini.advice_park.global.constant.Constants;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseDto<?> signUp(@RequestBody AuthSignUpDto authSignUpDto) {
        return ResponseDto.ok(authService.signUp(authSignUpDto));
    }

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody AuthLoginDto authLoginDto) {
        return ResponseDto.ok(authService.login(authLoginDto));
    }

    @GetMapping("/logout")
    public ResponseDto<?> logout(@SocialId String socialId) {
        return ResponseDto.ok(authService.logout(socialId));
    }

    @PostMapping("/refresh")
    public ResponseDto<?> updateAccessToken(@SocialId String socialId, HttpServletRequest request) {
        String refreshToken = request.getHeader(Constants.REAUTHORIZATION);
        return ResponseDto.ok(authService.reissue(socialId, refreshToken));
    }

    @GetMapping("/mail")
    public ResponseDto<String> sendCode(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        return ResponseDto.ok(emailService.sendCode(email));
    }

    @PostMapping("/mail")
    public ResponseDto<Boolean> verifyCode(@RequestBody EmailVerifyDto emailVerifyDto) {
        return ResponseDto.ok(emailService.verifyCode(emailVerifyDto));
    }
}
