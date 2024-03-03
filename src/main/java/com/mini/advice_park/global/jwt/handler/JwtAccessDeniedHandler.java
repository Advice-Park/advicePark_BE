package com.mini.advice_park.global.jwt.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰이 없거나 유효하지 않을 때 403 Forbidden 에러를 리턴
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

}
