package com.mini.advice_park.oauth2.handler;

import com.mini.advice_park.jwt.domain.Jwt;
import com.mini.advice_park.jwt.domain.JwtProvider;
import com.mini.advice_park.oauth2.config.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mini.advice_park.oauth2.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 인증 성공시 JWT AccessToken 과 RefreshToken 을 생성하여
 * 최초에 요청한 redirect_uri 파라미터 값의 주소에
 * access_token, refresh_token 쿼리 파라미터로 리디렉션
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Jwt token = jwtProvider.createToken(authentication);

        if (token == null) {
            throw new InternalAuthenticationServiceException("Failed to create token");
        }

        // 쿠키에 access_token, refresh_token 저장
        CookieUtils.addCookie(response, "access_token", token.getAccessToken(), (int) JwtProvider.ACCESS_TOKEN_EXPIRE_TIME);
        CookieUtils.addCookie(response, "refresh_token", token.getRefreshToken(), (int) JwtProvider.REFRESH_TOKEN_EXPIRE_TIME);

        clearAuthenticationAttributes(request, response);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
