package com.mini.advice_park.security.domain;

import com.mini.advice_park.member.domain.OAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserPrincipal extends User implements UserProvider {

    public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.LOCAL;
    }
}
