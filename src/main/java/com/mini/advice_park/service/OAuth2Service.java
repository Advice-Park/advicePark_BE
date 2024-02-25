package com.mini.advice_park.service;

import com.mini.advice_park.domain.User;
import com.mini.advice_park.dto.jwt.JwtTokenDto;
import com.mini.advice_park.constant.Constants;
import com.mini.advice_park.exception.CustomException;
import com.mini.advice_park.exception.ErrorCode;
import com.mini.advice_park.security.jwt.JwtProvider;
import com.mini.advice_park.repo.UserRepository;
import com.mini.advice_park.type.ELoginProvider;
import com.mini.advice_park.type.EUserType;
import com.mini.advice_park.utility.AuthCodeUtil;
import com.mini.advice_park.utility.CookieUtil;
import com.mini.advice_park.utility.GoogleOAuth2Util;
import com.mini.advice_park.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2Service {

    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;
    private final GoogleOAuth2Util oAuth2Util;
    private final UserRepository userRepository;

    public String getRedirectUrl(ELoginProvider provider) {
        if (provider == ELoginProvider.GOOGLE) {
            return oAuth2Util.getGoogleRedirectUrl();
        }
        return null;
    }

    public String getAccessToken(String authorizationCode, ELoginProvider provider) {
        String accessToken = null;
        if (provider == ELoginProvider.GOOGLE) {
            accessToken = oAuth2Util.getGoogleAccessToken(authorizationCode);
        }
        return accessToken;
    }

    public JwtTokenDto login(String accessToken, ELoginProvider provider) {
        String tempId = null;
        if (provider == ELoginProvider.GOOGLE) {
            tempId = oAuth2Util.getGoogleUserInfo(accessToken);
        }

        if (tempId == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_ERROR);
        }

        final String socialId = tempId;

        User user = userRepository.findBySocialIdAndProvider(socialId, provider)
                .orElseGet(() -> userRepository.save(User.builder()
                        .socialId(socialId)
                        .password(AuthCodeUtil.generateRandomPassword())
                        .userType(EUserType.USER)
                        .provider(provider)
                        .build()));

        JwtTokenDto jwtToken = jwtProvider.createTokens(user.getSocialId(), user.getUserType());
        log.info("jwtToken: {}", jwtToken);
        user.updateRefreshToken(jwtToken.refreshToken());

        return jwtToken;
    }

    public void sendAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);

        CookieUtil.addCookie(response, Constants.AUTHORIZATION, accessToken);
        CookieUtil.addSecureCookie(response, Constants.REAUTHORIZATION, refreshToken, jwtUtil.getRefreshTokenExpiration());

        response.sendRedirect("http://localhost:3000");
    }

    public Boolean logout(String socialId) {
        User user = userRepository.findBySocialIdAndIsLogin(socialId, true)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        user.logout();
        return true;
    }

    public Map<String, String> reissueAccessToken(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.AUTHORIZATION, jwtProvider.validateRefreshToken(request));
        return map;
    }
}
