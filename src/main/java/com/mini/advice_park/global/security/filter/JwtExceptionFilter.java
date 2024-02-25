package com.mini.advice_park.global.security.filter;

import com.mini.advice_park.global.constant.Constants;
import com.mini.advice_park.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.error("JwtExceptionFilter");
        request.setAttribute("exception", ErrorCode.UNAUTHORIZED_ERROR);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Constants.NO_AUTH_WHITE_LABEL.contains(request.getRequestURI()) || request.getRequestURI().startsWith("/guest");
    }
}
