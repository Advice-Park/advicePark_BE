package com.mini.advice_park.global.security.refreshToken;

import com.mini.advice_park.domain.oauth2.domain.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefreshToken 저장 및 조회 갱신
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveOrUpdate(OAuth2Provider oAuth2Provider, String email, String token) {

        refreshTokenRepository.findByEmailAndOAuth2Provider(email, oAuth2Provider).ifPresentOrElse(
                refreshToken -> refreshToken.changeToken(token), () -> {
                    RefreshToken refreshToken = RefreshToken.of(oAuth2Provider, email, token);
                    refreshTokenRepository.save(refreshToken);
                }
        );
    }

}
