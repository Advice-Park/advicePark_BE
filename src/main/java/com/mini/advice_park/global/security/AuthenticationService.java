package com.mini.advice_park.global.security;

import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * 쿠키에서 로그인한 사용자 정보 가져오기
     */
    public Optional<User> getLoggedInUserFromCookie(HttpServletRequest request) {

        // 쿠키에서 사용자 토큰 가져오기
        Optional<Cookie> tokenCookie = CookieUtils.getCookie(request, "token");

        if (tokenCookie.isPresent()) {
            Cookie cookie = tokenCookie.get();
            String token = cookie.getValue();

            // JWT 토큰에서 사용자 정보 가져오기
            String email = jwtUtil.getEmail(token);

            Optional<User> user = userRepository.findByEmail(email);

            if (user == null) {
                // 사용자 정보가 없는 경우 예외 처리
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }
            return user;

        } else {
            // 쿠키에서 토큰이 없는 경우 예외 처리
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
    }

}
