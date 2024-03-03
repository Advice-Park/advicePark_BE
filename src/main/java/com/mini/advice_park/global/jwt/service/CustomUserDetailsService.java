package com.mini.advice_park.global.jwt.service;

import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.oauth2.domain.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Spring Security 에서 사용자 정보를 가져오는 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().name());

        return new UserPrincipal(user.getEmail(),
                user.getPassword(),
                Collections.singletonList(grantedAuthority));
    }
}
