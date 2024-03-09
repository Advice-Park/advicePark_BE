package com.mini.advice_park.global.security;

import com.mini.advice_park.domain.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    /**
     * 쿠키에서 로그인한 사용자 정보 가져오기
     */
    public User getLoggedInUserFromCookie(HttpServletRequest request) {

        // 쿠키에서 사용자 정보를 추출하는 로직 구현
        Optional<Cookie> userCookie = CookieUtils.getCookie(request, "userCookieName");

        if (userCookie.isPresent()) {
            Cookie cookie = userCookie.get();
            // 쿠키에서 사용자 정보를 역직렬화하여 User 객체로 변환
            User user = CookieUtils.deserialize(cookie, User.class);
            return user;
        } else {
            // 쿠키에서 사용자 정보가 없는 경우 처리
            return null;
        }
    }

}
