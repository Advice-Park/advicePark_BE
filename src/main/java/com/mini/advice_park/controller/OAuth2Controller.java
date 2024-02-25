package com.mini.advice_park.controller;

import com.mini.advice_park.annotation.SocialId;
import com.mini.advice_park.dto.exception.ResponseDto;
import com.mini.advice_park.dto.jwt.JwtTokenDto;
import com.mini.advice_park.service.OAuth2Service;
import com.mini.advice_park.type.ELoginProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    /**
     * 최초 구글 로그인 시도 시, 구글 로그인 페이지로 리다이렉트
     */
    @GetMapping("/google")
    public ResponseDto<Map<String, String>> getGoogleRedirectUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("url", oAuth2Service.getRedirectUrl(ELoginProvider.GOOGLE));
        return ResponseDto.ok(map);
    }

    /**
     * 구글 로그인 후, 인가코드를 받아서 액세스 토큰을 발급받는다.
     */
    @GetMapping("/callback/google")
    public ResponseDto<JwtTokenDto> getGoogleAccessToken(String code) {
        String accessToken = oAuth2Service.getAccessToken(code, ELoginProvider.GOOGLE);
        return ResponseDto.created(oAuth2Service.login(accessToken, ELoginProvider.GOOGLE));
    }

    /**
     * 소셜 로그인 로그아웃
     */
    @GetMapping("/logout")
    public ResponseDto<Boolean> logout(@SocialId String socialId) {
        return ResponseDto.ok(oAuth2Service.logout(socialId));
    }

    /**
     * 액세스 토큰 갱신
     * 소셜 사용자만 가능
     */
    @PostMapping("/refresh")
    public ResponseDto<?> updateAccessToken(HttpServletRequest request) {
        return ResponseDto.ok(oAuth2Service.reissueAccessToken(request));
    }
}
