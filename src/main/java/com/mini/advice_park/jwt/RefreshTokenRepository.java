package com.mini.advice_park.jwt;

import com.mini.advice_park.jwt.domain.RefreshToken;
import com.mini.advice_park.member.domain.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // Optional<RefreshToken> findByEmailAndOAuth2Provider(String email, OAuth2Provider oAuth2Provider);

    @Query("SELECT r FROM RefreshToken r WHERE r.email = :email AND r.oAuth2Provider = :provider")
    Optional<RefreshToken> findByEmailAndOAuth2Provider(@Param("email") String email, @Param("provider") OAuth2Provider oAuth2Provider);

}
