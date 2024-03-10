package com.mini.advice_park.global.security;

import com.mini.advice_park.domain.oauth2.domain.OAuth2UserPrincipal;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    /**
     * 쿠키에서 소셜로그인한 사용자 정보 가져오기
     */
    public OAuth2UserPrincipal getLoggedInUserFromCookie(HttpServletRequest request) {

        // 쿠키에서 사용자 정보를 추출하는 로직 구현
        Optional<Cookie> userCookie = CookieUtils.getCookie(request, "token");

        if (userCookie.isPresent()) {
            Cookie cookie = userCookie.get();
            // 쿠키에서 사용자 정보를 역직렬화하여 OAuth2UserPrincipal 객체로 변환
            return CookieUtils.deserialize(cookie, OAuth2UserPrincipal.class);

        } else {
            // 쿠키에서 사용자 정보가 없는 경우 예외 처리
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
    }

}
