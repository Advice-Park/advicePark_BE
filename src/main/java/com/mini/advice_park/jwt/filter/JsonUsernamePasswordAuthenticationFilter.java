package com.mini.advice_park.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.advice_park.member.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 일반 사용자 로그인을 처리하고 JWT 토큰을 발급합니다.
 */
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 사용자 인증이 필요한지 여부를 결정하는 URL을 설정합니다.
     */
    public JsonUsernamePasswordAuthenticationFilter(String filterProcessUrl,
                                                    AuthenticationManager authenticationManager,
                                                    AuthenticationSuccessHandler successHandler,
                                                    AuthenticationFailureHandler failureHandler) {
        super(filterProcessUrl, authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    /**
     * 사용자가 제공한 정보를 사용하여 인증을 시도합니다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new AuthenticationServiceException("Authentication parameter not supported");
        }
    }

}
