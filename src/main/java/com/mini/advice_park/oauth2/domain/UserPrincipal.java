package com.mini.advice_park.oauth2.domain;

import com.mini.advice_park.user.entity.OAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 일반 사용자 정보를 담는 클래스
 */
public class UserPrincipal extends User implements UserProvider {

    public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.LOCAL;
    }

}
