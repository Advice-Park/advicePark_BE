package com.mini.advice_park.global.security.filter;

import com.mini.advice_park.global.security.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰을 인증 정보로 변환하여 SecurityContext에 저장
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    // JWT 토큰을 인증 정보로 변환하여 SecurityContext에 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = resolveToken(request);

            if (StringUtils.hasText(token)) {
                jwtUtil.validateToken(token);
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    // 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

}
