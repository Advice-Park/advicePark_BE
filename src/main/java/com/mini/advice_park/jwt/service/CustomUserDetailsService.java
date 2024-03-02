package com.mini.advice_park.jwt.service;

import com.mini.advice_park.member.domain.Member;
import com.mini.advice_park.member.repository.MemberRepository;
import com.mini.advice_park.security.domain.UserPrincipal;
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

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().name());

        return new UserPrincipal(member.getEmail(),
                member.getPassword(),
                Collections.singletonList(grantedAuthority));
    }
}
