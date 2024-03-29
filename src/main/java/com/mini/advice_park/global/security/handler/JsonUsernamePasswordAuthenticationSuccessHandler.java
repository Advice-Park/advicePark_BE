package com.mini.advice_park.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import com.mini.advice_park.global.security.jwt.JwtDto;
import com.mini.advice_park.domain.user.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 로그인 성공 시 JWT 토큰을 생성하여 응답
 */
@Component
@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        JwtDto token = jwtUtil.createToken(authentication);

        LoginResponse loginResponse = LoginResponse.of(token.getAccessToken(), token.getRefreshToken());

        try {
            jwtUtil.saveRefreshToken(authentication, token.getRefreshToken());

            String tokenResponse = objectMapper.writeValueAsString(loginResponse);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(tokenResponse);
            response.setStatus(HttpServletResponse.SC_CREATED);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
