package com.mini.advice_park.domain.user.entity;

import com.mini.advice_park.domain.oauth2.domain.OAuth2Provider;
import com.mini.advice_park.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 회원 엔티티
 */
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider oAuth2Provider;

    private String providerId;
    private String email;
    private String password;
    private String name;
    private String firstName;
    private String lastName;
    private String nickname;
    private String image;
    private Boolean withdrawal; // 탈퇴여부

    private User(OAuth2Provider oAuth2Provider,
                 String providerId,
                 String email,
                 String password,
                 String name,
                 String firstName,
                 String lastName,
                 String nickname,
                 String image,
                 Role role) {
        this.oAuth2Provider = oAuth2Provider;
        this.providerId = providerId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.image = image;
        this.withdrawal = false;
        this.role = role;
    }

    public static User of(OAuth2Provider oAuth2Provider,
                          String providerId,
                          String email,
                          String name,
                          String firstName,
                          String lastName,
                          String nickname,
                          String image) {
        return new User(oAuth2Provider,
                providerId,
                email,
                null,
                name,
                firstName,
                lastName,
                nickname,
                image,
                Role.USER);
    }

    public static User of(String email,
                          String password,
                          String firstName,
                          String lastName,
                          String nickname,
                          String image,
                          PasswordEncoder encoder) {
        return new User(OAuth2Provider.LOCAL,
                null,
                email,
                encoder.encode(password),
                firstName + " " + lastName,
                firstName,
                lastName,
                nickname,
                image,
                Role.USER);
    }

    /**
     * 기본 관리자 계정 생성
     */
    public static User createDefaultAdmin(PasswordEncoder encoder) {
        return new User(OAuth2Provider.LOCAL,
                null,
                "admin@admin.com",
                encoder.encode("admin"),
                "admin",
                null,
                null,
                "admin",
                null,
                Role.ADMIN);
    }

    public void update(String name,
                       String firstName,
                       String lastName,
                       String nickname,
                       String image) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.image = image;
    }
}
