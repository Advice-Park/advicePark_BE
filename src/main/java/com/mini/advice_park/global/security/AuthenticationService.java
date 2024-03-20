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

        Optional<Cookie> tokenCookie = CookieUtils.getCookie(request, "token");

        if (tokenCookie.isPresent()) {
            Cookie cookie = tokenCookie.get();
            String token = cookie.getValue();

            String email = jwtUtil.getEmail(token);

            Optional<User> user = userRepository.findByEmail(email);

            if (user == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }
            return user;

        } else {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
    }

}
