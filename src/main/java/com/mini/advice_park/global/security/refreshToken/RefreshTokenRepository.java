package com.mini.advice_park.global.security.refreshToken;

import com.mini.advice_park.domain.oauth2.domain.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT r FROM RefreshToken r WHERE r.email = :email AND r.oAuth2Provider = :provider")
    Optional<RefreshToken> findByEmailAndOAuth2Provider(@Param("email") String email, @Param("provider") OAuth2Provider oAuth2Provider);

}
