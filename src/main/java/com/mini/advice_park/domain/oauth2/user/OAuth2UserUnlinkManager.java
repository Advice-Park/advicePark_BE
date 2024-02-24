package com.mini.advice_park.domain.oauth2.user;

import com.mini.advice_park.domain.oauth2.exception.OAuth2AuthenticationProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2UserUnlinkManager {

    /**
     * 제공자 별로 분기하여 연동 해제
     */

    private final GoogleOAuth2UserUnlink googleOAuth2UserUnlink;

    public void unlink(OAuth2Provider provider, String accessToken) {
        if (OAuth2Provider.GOOGLE.equals(provider)) {
            googleOAuth2UserUnlink.unlink(accessToken);
        } else {
            throw new OAuth2AuthenticationProcessingException(
                    "Unlink with " + provider.getRegistrationId() + " is not supported");
        }
    }
}
