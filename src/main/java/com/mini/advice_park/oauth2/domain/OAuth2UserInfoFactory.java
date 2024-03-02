package com.mini.advice_park.oauth2.domain;

import com.mini.advice_park.user.entity.OAuth2Provider;
import com.mini.advice_park.oauth2.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

/**
 * OAuth2 공급자에 따라 인증된 사용자의 정보를 처리하여 해당하는 OAuth2UserInfo 구현체를 리턴합니다.
 */
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
                                                    Map<String, Object> attributes) {

        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleOAuth2UserInfo(attributes);

        } else if (OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) {
            return new NaverOAuth2UserInfo(attributes);

        } else if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            return new KakaoOAuth2UserInfo(attributes);

        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }

}
