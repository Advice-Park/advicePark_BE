package com.mini.advice_park.domain.oauth2.domain;

import com.mini.advice_park.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * OAuth2 로그인 사용자 정보를 담는 클래스
 */
@AllArgsConstructor
public class OAuth2UserPrincipal implements OAuth2User, UserDetails, UserProvider {

    private final OAuth2Provider provider;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    /**
     * 소셜 로그인 사용자 정보를 담는 클래스를 생성
     * @param user
     * @param attributes
     * @return
     */
    public static OAuth2UserPrincipal create(User user, Map<String, Object> attributes) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(user.getRole().name()));

        return new OAuth2UserPrincipal(
                user.getOAuth2Provider(),
                user.getEmail(),
                user.getPassword(),
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
