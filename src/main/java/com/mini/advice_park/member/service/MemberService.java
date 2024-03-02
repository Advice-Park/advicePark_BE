package com.mini.advice_park.member.service;

import com.mini.advice_park.member.domain.Member;
import com.mini.advice_park.member.repository.MemberRepository;
import com.mini.advice_park.member.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원가입 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(SignUpRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String nickname = request.getNickname();
        String image = request.getImage();

        Member member = Member.of(email, password, firstName, lastName, nickname, image, passwordEncoder);

        memberRepository.save(member);
    }

}
