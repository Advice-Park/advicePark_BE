package com.mini.advice_park.security.domain;

import com.mini.advice_park.member.domain.Member;
import com.mini.advice_park.member.domain.OAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OAuth2UserPrincipal implements OAuth2User, UserDetails, UserProvider {

    private final OAuth2Provider provider;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public OAuth2UserPrincipal(OAuth2Provider provider,
                               String email,
                               String password,
                               Collection<? extends GrantedAuthority> authorities,
                               Map<String, Object> attributes) {
        this.provider = provider;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    public static OAuth2UserPrincipal create(Member member, Map<String, Object> attributes) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(member.getRole().name()));

        return new OAuth2UserPrincipal(
                member.getOAuth2Provider(),
                member.getEmail(),
                member.getPassword(),
                authorities,
                attributes);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public OAuth2Provider getProvider() {
        return provider;
    }
}
