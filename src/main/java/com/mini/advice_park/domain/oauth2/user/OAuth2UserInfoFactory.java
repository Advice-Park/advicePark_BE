package com.mini.advice_park.domain.oauth2.user;

import com.mini.advice_park.domain.oauth2.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    /**
     * Oauth2 인증시 액세스 토큰으로 사용자 정보를 가져왔을 때, 제공자 별로 분기하여 객체를 생성
     */
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
                                                   String accessToken,
                                                   Map<String, Object> attributes) {
        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleOAuth2UserInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}