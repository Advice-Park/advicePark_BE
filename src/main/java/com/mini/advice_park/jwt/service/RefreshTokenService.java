package com.mini.advice_park.jwt.service;

import com.mini.advice_park.jwt.RefreshTokenRepository;
import com.mini.advice_park.jwt.domain.RefreshToken;
import com.mini.advice_park.user.entity.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefreshToken 저장 및 조회 서비스
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
