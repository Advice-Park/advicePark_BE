package com.mini.advice_park.domain.oauth2.user;

public interface OAuth2UserUnlink {

    /** Oauth2 제공자 별로 애플리케이션과 연동 해제 하는 방법 다름.
     * 서비스 별로 다른 연동 해제 방법을 통합하기 위한 인터페이스
     */
    void unlink(String accessToken);
}
