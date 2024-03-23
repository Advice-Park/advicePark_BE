package com.mini.advice_park.domain.user.service;

import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.global.security.filter.JwtAuthorizationFilter;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * 현재 사용자 정보 가져오기
     * 인증방식
     */
    @Transactional
    public User getCurrentUser(HttpServletRequest httpServletRequest) {

        String token = JwtAuthorizationFilter.resolveToken(httpServletRequest);
        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        String email = jwtUtil.getEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ERROR));
    }

}
