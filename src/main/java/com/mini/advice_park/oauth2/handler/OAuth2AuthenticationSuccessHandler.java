package com.mini.advice_park.oauth2.handler;

import com.mini.advice_park.jwt.domain.Jwt;
import com.mini.advice_park.jwt.domain.JwtProvider;
import com.mini.advice_park.jwt.exception.InvalidTokenException;
import com.mini.advice_park.oauth2.config.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mini.advice_park.oauth2.util.CookieUtils;
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

import static com.mini.advice_park.oauth2.config.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * OAuth2 인증 성공시 JWT AccessToken 과 RefreshToken 을 생성하여
 * 최초에 요청한 redirect_uri 파라미터 값의 주소에
 * access_token, refresh_token 쿼리 파라미터로 리디렉션합니다.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        try {
            targetUrl = determineTargetUrl(request, response, authentication);
        } catch (InvalidTokenException e) {
            throw new InternalAuthenticationServiceException("Authentication Principal is not of type UserProvider");
        }

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        Jwt token = jwtProvider.createToken(authentication);

        jwtProvider.saveRefreshToken(authentication, token.getRefreshToken());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", token.getAccessToken())
                .queryParam("refresh_token", token.getRefreshToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
