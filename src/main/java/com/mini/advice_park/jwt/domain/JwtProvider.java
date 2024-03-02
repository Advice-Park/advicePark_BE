package com.mini.advice_park.jwt.domain;

import com.mini.advice_park.global.exception.CustomException;
import com.mini.advice_park.global.exception.ErrorCode;
import com.mini.advice_park.jwt.service.RefreshTokenService;
import com.mini.advice_park.user.entity.OAuth2Provider;
import com.mini.advice_park.security.domain.UserProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 12;
    public static final String PROVIDER_CLAIM_NAME = "provider";
    public static final String AUTHORITIES_CLAIM_NAME = "authorities";
    private final RefreshTokenService refreshTokenService;

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] key = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(key);
    }

    /**
     * JWT 토큰 생성
     */
    public Jwt createToken(Authentication authentication) {

        Object principal = authentication.getPrincipal();

        OAuth2Provider oAuth2Provider;

        if (principal instanceof UserProvider) {
            UserProvider userProvider = (UserProvider) principal;
            oAuth2Provider =  userProvider.getProvider();
        } else {
            throw new IllegalArgumentException();
        }

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        // 엑세스 토큰 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(PROVIDER_CLAIM_NAME, oAuth2Provider.getRegistrationId())
                .claim(AUTHORITIES_CLAIM_NAME, authorities)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        // 리프레시 토큰 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(PROVIDER_CLAIM_NAME, oAuth2Provider.getRegistrationId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new Jwt(accessToken, refreshToken);
    }

    public void saveRefreshToken(Authentication authentication, String token) {
        String email = authentication.getName();
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserProvider)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        UserProvider provider = (UserProvider) principal;
        refreshTokenService.saveOrUpdate(provider.getProvider(), email, token);
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public OAuth2Provider getProvider(String token) {
        String providerRegistrationId = (String) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(PROVIDER_CLAIM_NAME);

        return Arrays.stream(OAuth2Provider.values())
                .filter(authProvider -> authProvider.getRegistrationId().equals(providerRegistrationId))
                .findAny()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT is not valid");
        } catch (SignatureException exception) {
            log.error("JWT signature validation fails");
        } catch (ExpiredJwtException exception) {
            log.error("JWT is expired");
        } catch (IllegalArgumentException exception) {
            log.error("JWT is null or empty or only whitespace");
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }

        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (claims.get(PROVIDER_CLAIM_NAME) == null || claims.get(AUTHORITIES_CLAIM_NAME) == null) {
            throw new IllegalArgumentException();
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_CLAIM_NAME).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails user = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

}
