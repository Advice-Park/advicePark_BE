package com.mini.advice_park.domain.oauth2.domain;

import java.util.Map;

/**
 * OAuth2 로그인 시 사용자 정보를 담는 인터페이스
 */
public interface OAuth2UserInfo {

    String getId();
    String getNameAttributeKey();
    Map<String, Object> getAttributes();
    String getEmail();
    String getName();
    String getFirstName();
    String getLastName();
    String getNickname();
    String getImage();

}
