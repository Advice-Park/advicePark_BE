package com.mini.advice_park.domain.user.dto;

import com.mini.advice_park.domain.user.entity.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo {

    /**
     * 회원 정보 반환
     */
    private final Long userId;
    private final String providerId;
    private final Role role;
    private final String email;
    private final String name;
    private final String image;

    public static UserInfo of(Long userId,
                              String providerId,
                              Role role,
                              String email,
                              String name,
                              String image) {
        return new UserInfo(userId, providerId, role, email, name, image);
    }

}
