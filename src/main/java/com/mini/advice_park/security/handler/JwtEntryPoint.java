package com.mini.advice_park.security.handler;

import com.mini.advice_park.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.mini.advice_park.security.handler.JwtAccessDeniedHandler.setErrorResponse;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        if (errorCode == null) {
            setErrorResponse(response, ErrorCode.FORBIDDEN_ERROR);
        } else {
            switch (errorCode) {
                case UNAUTHORIZED_ERROR -> {setErrorResponse(response, ErrorCode.UNAUTHORIZED_ERROR);}
                case FORBIDDEN_ERROR -> {setErrorResponse(response, ErrorCode.FORBIDDEN_ERROR);}
                default -> {setErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);}
            }
        }
    }
}