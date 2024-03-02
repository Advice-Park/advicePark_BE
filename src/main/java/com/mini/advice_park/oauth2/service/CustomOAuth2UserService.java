package com.mini.advice_park.oauth2.service;

import com.mini.advice_park.member.domain.Member;
import com.mini.advice_park.member.domain.OAuth2Provider;
import com.mini.advice_park.member.repository.MemberRepository;
import com.mini.advice_park.oauth2.domain.OAuth2UserInfo;
import com.mini.advice_park.oauth2.domain.OAuth2UserInfoFactory;
import com.mini.advice_park.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.mini.advice_park.security.domain.OAuth2UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    /**
     * OAuth2 사용자 정보의 유효성을 검사하고 DB 에 저장합니다.
     * @param oAuth2UserRequest OAuth2 요청
     * @return OAuth2User OAuth2 사용자 정보
     * @throws OAuth2AuthenticationException
     *         OAuth2 사용자 정보가 유효하지 않을 때
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * OAuth2 사용자 정보의 유효성을 검사하고 DB 에 저장합니다.
     * @param userRequest OAuth2 요청
     * @param oAuth2User OAuth2 사용자 정보
     * @return OAuth2UserPrincipal
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,
                oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        OAuth2Provider oAuth2Provider = Arrays.stream(OAuth2Provider.values())
                .filter(authProvider -> authProvider.getRegistrationId().equals(registrationId))
                .findAny()
                .orElseThrow(() -> new OAuth2AuthenticationProcessingException("Registration ID not supported"));

        Optional<Member> memberOptional = memberRepository.findByEmailAndOAuth2Provider(oAuth2UserInfo.getEmail(), oAuth2Provider);

        Member member;

        if (memberOptional.isPresent()) {
            member = memberOptional.get();

            if (!member.getProviderId().equals(oAuth2UserInfo.getId())) {
                throw new OAuth2AuthenticationProcessingException("Provider ID is invalid");
            }

            member = updateExistingUser(member, oAuth2UserInfo);
        } else {
            member = registerNewUser(oAuth2Provider, oAuth2UserInfo);
        }

        return OAuth2UserPrincipal.create(member, oAuth2UserInfo.getAttributes());
    }

    /**
     * 새로운 OAuth2 인증된 사용자를 DB 에 저장합니다.
     * @param provider OAuth2 공급자
     * @param oAuth2UserInfo OAuth2 사용자 정보
     * @return Member
     */
    private Member registerNewUser(OAuth2Provider provider, OAuth2UserInfo oAuth2UserInfo) {
        Member member = Member.of(provider,
                oAuth2UserInfo.getId(),
                oAuth2UserInfo.getEmail(),
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getFirstName(),
                oAuth2UserInfo.getLastName(),
                oAuth2UserInfo.getNickname(),
                oAuth2UserInfo.getImage());

        return memberRepository.save(member);
    }

    /**
     * DB 에 존재하는 OAuth2 인증된 사용자 정보를 업데이트합니다.
     * @param member DB 에 존재하는 사용자 정보
     * @param oAuth2UserInfo OAuth2 사용자 정보
     * @return Member
     */
    private Member updateExistingUser(Member member, OAuth2UserInfo oAuth2UserInfo) {
        member.update(oAuth2UserInfo.getName(),
                oAuth2UserInfo.getFirstName(),
                oAuth2UserInfo.getLastName(),
                oAuth2UserInfo.getNickname(),
                oAuth2UserInfo.getImage());
        return memberRepository.save(member);
    }

}
