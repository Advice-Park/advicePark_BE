package com.mini.advice_park.domain.oauth2.handler;

import com.mini.advice_park.domain.oauth2.config.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mini.advice_park.domain.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.mini.advice_park.global.security.jwt.JwtDto;
import com.mini.advice_park.global.security.jwt.JwtUtil;
import com.mini.advice_park.global.security.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /**
     * OAuth2 인증 성공시 JWT AccessToken 과 RefreshToken 을 생성하여
     * 최초에 요청한 redirect_uri 파라미터 값의 주소에 access_token, refresh_token 쿼리 파라미터로 리디렉션
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        try {
            targetUrl = determineTargetUrl(request, response, authentication);
        } catch (OAuth2AuthenticationProcessingException e) {
            throw new InternalAuthenticationServiceException("Authentication Principal is not of type UserProvider");
        }

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * OAuth2 인증 성공시 리디렉션할 URL을 결정합니다.
     */
    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws OAuth2AuthenticationProcessingException {
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        // 리다이렉션 URL의 유효성 검사
        if (!isValidRedirectUrl(targetUrl)) {
            throw new OAuth2AuthenticationProcessingException("Invalid redirect URL: " + targetUrl);
        }

        JwtDto token = jwtUtil.createToken(authentication);
        jwtUtil.saveRefreshToken(authentication, token.getRefreshToken());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", token.getAccessToken())
                .queryParam("refresh_token", token.getRefreshToken())
                .build().toUriString();
    }

    /**
     * 주어진 URL이 유효한지 확인합니다.
     * 실제로 사용할 수 있는 유효성 검사를 수행
     */
    private boolean isValidRedirectUrl(String redirectUrl) {
        // 주어진 URL이 null이 아니고 비어 있지 않은지 확인
        return redirectUrl != null && !redirectUrl.isEmpty();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}