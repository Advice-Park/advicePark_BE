package com.mini.advice_park.domain.oauth2;

import com.mini.advice_park.domain.oauth2.domain.OAuth2UserInfo;
import com.mini.advice_park.domain.oauth2.domain.OAuth2UserInfoFactory;
import com.mini.advice_park.domain.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.oauth2.domain.OAuth2Provider;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.oauth2.domain.OAuth2UserPrincipal;
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


/**
 * OAuth2 사용자 정보의 유효성을 검사하고 DB 에 저장합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /**
     * OAuth2 사용자 정보의 유효성을 검사하고 DB에 저장합니다.
     * @param oAuth2UserRequest OAuth2 요청
     * @return OAuth2UserPrincipal
     * @throws OAuth2AuthenticationException OAuth2 사용자 정보가 유효하지 않을 때
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

        Optional<User> userOptional = userRepository.findByEmailAndOAuth2Provider(oAuth2UserInfo.getEmail(), oAuth2Provider);

        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();

            if (!user.getProviderId().equals(oAuth2UserInfo.getId())) {
                throw new OAuth2AuthenticationProcessingException("Provider ID is invalid");
            }

            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2Provider, oAuth2UserInfo);
        }

        return OAuth2UserPrincipal.create(user, oAuth2UserInfo.getAttributes());
    }

    /**
     * 새로운 OAuth2 인증된 사용자를 DB 에 저장합니다.
     * @param provider OAuth2 공급자
     * @param oAuth2UserInfo OAuth2 사용자 정보
     * @return Member
     */
    private User registerNewUser(OAuth2Provider provider, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.of(provider,
                oAuth2UserInfo.getId(),
                oAuth2UserInfo.getEmail(),
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getFirstName(),
                oAuth2UserInfo.getLastName(),
                oAuth2UserInfo.getNickname(),
                oAuth2UserInfo.getImage());

        return userRepository.save(user);
    }

    /**
     * DB 에 존재하는 OAuth2 인증된 사용자 정보를 업데이트합니다.
     * @param user DB 에 존재하는 사용자 정보
     * @param oAuth2UserInfo OAuth2 사용자 정보
     * @return Member
     */
    private User updateExistingUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.update(oAuth2UserInfo.getName(),
                oAuth2UserInfo.getFirstName(),
                oAuth2UserInfo.getLastName(),
                oAuth2UserInfo.getNickname(),
                oAuth2UserInfo.getImage());
        return userRepository.save(user);
    }

    public User getUserFromOAuth2Principal(OAuth2UserPrincipal oauth2UserPrincipal) {

        // OAuth2UserPrincipal에서 필요한 사용자 정보를 추출
        OAuth2Provider provider = oauth2UserPrincipal.getProvider();
        String email = oauth2UserPrincipal.getUsername(); // 이메일 정보를 사용
        String password = oauth2UserPrincipal.getPassword(); // 비밀번호는 OAuth2 사용자 정보에 저장되어 있지 않음
        // OAuth2 사용자 정보에는 authorities가 포함되어 있지만, 이 예제에서는 사용하지 않음

        // OAuth2 사용자 정보에서 추가적인 필요한 정보가 있다면 attributes에서 추출하여 사용

        // 이메일을 기준으로 DB에서 사용자를 찾거나, 없으면 새로운 사용자를 생성
        Optional<User> userOptional = userRepository.findByEmailAndOAuth2Provider(email, provider);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            // 기존 사용자 정보 업데이트 등의 추가 로직이 있다면 여기에 추가
        } else {
            // 새로운 사용자 생성
            user = User.of(provider,
                    oauth2UserPrincipal.getAttributes().get("id").toString(), // OAuth2에서 제공하는 고유 ID 사용
                    email,
                    oauth2UserPrincipal.getAttributes().get("name").toString(),
                    oauth2UserPrincipal.getAttributes().get("given_name").toString(),
                    oauth2UserPrincipal.getAttributes().get("family_name").toString(),
                    oauth2UserPrincipal.getAttributes().get("nickname").toString(),
                    oauth2UserPrincipal.getAttributes().get("picture").toString());
            userRepository.save(user);
        }

        return user;
    }


}
