package com.mini.advice_park.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.advice_park.jwt.domain.Jwt;
import com.mini.advice_park.jwt.domain.JwtProvider;
import com.mini.advice_park.user.dto.LoginResponse;
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

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        Jwt token = jwtProvider.createToken(authentication);

        LoginResponse loginResponse = LoginResponse.of(token.getAccessToken(), token.getRefreshToken());

        try {
            jwtProvider.saveRefreshToken(authentication, token.getRefreshToken());

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
