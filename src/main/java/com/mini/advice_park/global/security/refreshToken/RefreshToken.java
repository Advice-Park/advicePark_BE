package com.mini.advice_park.global.security.refreshToken;

import com.mini.advice_park.domain.oauth2.domain.OAuth2Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OAuth2Provider oAuth2Provider;

	private String email;
	private String token;

	private RefreshToken(OAuth2Provider oAuth2Provider, String email, String token) {
		this.oAuth2Provider = oAuth2Provider;
		this.email = email;
		this.token = token;
	}

	public static RefreshToken of(OAuth2Provider oAuth2Provider, String email, String token) {
		return new RefreshToken(oAuth2Provider, email, token);
	}

	public void changeToken(String token) {
		this.token = token;
	}

}
